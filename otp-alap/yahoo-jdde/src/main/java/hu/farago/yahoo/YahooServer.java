package hu.farago.yahoo;

/*
 * Copyright 2013 www.pretty-tools.com. All rights reserved.
 */

import hu.farago.yahoo.download.YahooCurrencPairDownloader;
import hu.farago.yahoo.dto.TickData;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.pretty_tools.dde.ClipboardFormat;
import com.pretty_tools.dde.DDEException;
import com.pretty_tools.dde.DDEMLException;
import com.pretty_tools.dde.server.DDEServer;

/**
 * DDE Server Example.
 */
public class YahooServer {

	private static final String SERVICE_NAME = "MyServer";

	private static final String LAST = "Last";
	private static final String TIME = "Time";
	private static final String VOLUME = "Volume";
	private static final String LOW = "Low";
	private static final String HIGH = "High";
	private static final String OPEN = "Open";

	private static final String LASTSIZE = "LastSize";
	private static final String ASK = "Ask";
	private static final String ASKSIZE = "AskSize";
	private static final String BID = "Bid";
	private static final String BIDSIZE = "BidSize";
	private static final String REQ = "Req";

	
	private static final List<String> ITEMS = Arrays.asList(OPEN, HIGH, LOW,
			VOLUME, TIME, LAST);
	private static final List<String> EXTRA_ITEMS = Arrays.asList(LASTSIZE, ASK, ASKSIZE, BID, BIDSIZE, REQ);

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		try {
			System.loadLibrary("JavaDDEx64");

			final CountDownLatch eventStop = new CountDownLatch(1);

			YahooCurrencPairDownloader downloader = new YahooCurrencPairDownloader();
			final List<TickData> tickDataList = Lists.newArrayList();

			final DDEServer server = new DDEServer(SERVICE_NAME) {

				@Override
				protected boolean isTopicSupported(String topicName) {
					for (String pair : downloader.getProperties().currencyPairs) {
						if (pair.equalsIgnoreCase(topicName)) {
							return true;
						}
					}
					return false;
				}

				@Override
				protected boolean isItemSupported(String topic, String item,
						int uFmt) {
					return isTopicSupported(topic)
							&& (ITEMS.contains(item) || EXTRA_ITEMS.contains(item))
							&& (uFmt == ClipboardFormat.CF_TEXT.getNativeCode() || uFmt == ClipboardFormat.CF_UNICODETEXT
									.getNativeCode());
				}

				@Override
				protected boolean onExecute(String command) {
					System.out.println("onExecute(" + command + ")");

					if ("stop".equalsIgnoreCase(command))
						eventStop.countDown();

					return true;
				}

				@Override
				protected boolean onPoke(String topic, String item, String data) {
					System.out.println("onPoke(" + topic + ", " + item + ", "
							+ data + ")");
					return true;
				}

				@Override
				protected boolean onPoke(String topic, String item,
						byte[] data, int uFmt) {
					System.out.println("onPoke(" + topic + ", " + item + ", "
							+ data + ", " + uFmt + ")");
					return false;
				}

				@Override
				protected String onRequest(String topic, String item) {
					System.out
							.println("onRequest(" + topic + ", " + item + ")");
					TickData currentTickData = Iterables.find(tickDataList,
							new Predicate<TickData>() {
								@Override
								public boolean apply(TickData data) {
									return topic.equalsIgnoreCase(data.getCurrencyPair());
								}
							});

					switch (item) {

					case LAST:
						return currentTickData.getLast().toString();
					case TIME:
						return currentTickData.getTime().toString();
					case VOLUME:
						return currentTickData.getVolume().toString();
					case HIGH:
						return currentTickData.getHigh().toString();
					case LOW:
						return currentTickData.getLow().toString();
					case OPEN:
						return currentTickData.getOpen().toString();
					default:
						return "unnecessary data: " + item;

					}

				}

				@Override
				protected byte[] onRequest(String topic, String item, int uFmt) {
					System.out.println("onPoke(" + topic + ", " + item + ", "
							+ uFmt + ")");
					return null;
				}
			};

			System.out.println("Starting...");
			server.start();
			
			Collection<String> UNION = CollectionUtils.union(ITEMS, EXTRA_ITEMS);

			while (true) {
				try {
					System.out.println("notify clients");

					tickDataList.clear();
					tickDataList.addAll(downloader.getAllTickData());

					for (TickData data : tickDataList) {
						for (String item : UNION) {
							System.out.println("notify: " + data.getCurrencyPair() + " on " + item);
							server.notifyClients(data.getCurrencyPair(), item);
						}
					}

				} catch (DDEException | IOException e) {
					System.out.println("Exception: " + e.getMessage());
					break;
				}
			}

			System.out.println("Waiting for stop...");
			eventStop.await();
			System.out.println("stopping...");
			server.stop();
			System.out.println("Exit from thread");
		} catch (DDEMLException e) {
			System.out.println("DDEMLException: 0x"
					+ Integer.toHexString(e.getErrorCode()) + " "
					+ e.getMessage());
		} catch (DDEException e) {
			System.out.println("DDEClientException: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}
