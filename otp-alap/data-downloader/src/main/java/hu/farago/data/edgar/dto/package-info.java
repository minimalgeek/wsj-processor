@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(value = DateTimeAdapter.class, type = DateTime.class)
})

package hu.farago.data.edgar.dto;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import hu.farago.data.edgar.adapter.DateTimeAdapter;
import org.joda.time.DateTime;