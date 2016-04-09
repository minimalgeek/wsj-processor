package hu.farago.data.edgar.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

@XmlRootElement(name = "ownershipDocument")
@XmlAccessorType(XmlAccessType.FIELD)
public class EdgarXML {

	public String schemaVersion;
	public int documentType;
	public DateTime periodOfReport;
	public int notSubjectToSection16;
	public Issuer issuer;
	public ReportingOwner reportingOwner;
	@XmlElementWrapper(name = "nonDerivativeTable")
    @XmlElement(name = "nonDerivativeTransaction")
	public List<NonDerivativeTransaction> nonDerivativeTable;
	@XmlElementWrapper(name = "derivativeTable")
    @XmlElement(name = "derivativeTransaction")
    public List<DerivativeTransaction> derivativeTable;
	public OwnerSignature ownerSignature;


}
