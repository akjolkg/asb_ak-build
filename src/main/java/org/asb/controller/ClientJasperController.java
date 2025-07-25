package org.asb.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.Message;
import org.asb.beans.SortEnum;
import org.asb.enums.AppartmentType;
import org.asb.enums.ClientFrom;
import org.asb.enums.ClientType;
import org.asb.enums.ContractTemplateType;
import org.asb.enums.ContractType;
import org.asb.enums.DocumentTemplateType;
import org.asb.enums.OfficeType;
import org.asb.enums.PaymentType;
import org.asb.model.Client;
import org.asb.model.ContractTemplate;
import org.asb.model.Denounce;
import org.asb.model.DocumentTemplate;
import org.asb.model.Payment;
import org.asb.model.Schedule;
import org.asb.model.ScheduleTemplate;
import org.asb.model.SubSchedule;
import org.asb.service.ClientService;
import org.asb.service.ContractTemplateService;
import org.asb.service.DocumentTemplateService;
import org.asb.service.PaymentService;
import org.asb.service.ScheduleService;
import org.asb.service.ScheduleTemplateService;
import org.asb.service.SubScheduleService;
import org.asb.util.Translit;
import org.asb.util.web.Messages;
import org.primefaces.model.DefaultStreamedContent;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleDocxExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsExporterConfiguration;

@RequestScoped
@ManagedBean
public class ClientJasperController extends BaseReportController {
	@Inject
	private JasperViewerController jasperViewerController;
	@EJB
	private ClientService service;
	@EJB
	private ScheduleService sService;
	@EJB
	private SubScheduleService ssService;
	@EJB
	private ScheduleTemplateService stService;
	@EJB
	private PaymentService pService;
	@EJB
	private ContractTemplateService ctService;
	@Inject
	private ContractDocController docController;
	
	@EJB
	private DocumentTemplateService dtService;

	private boolean value1;

	private Map<String, String> prepareClientDetailsForContract(Client client) {
		Map<String, String> map = new HashMap<>();
		map = new HashMap<>();
		RuleBasedNumberFormat nff = new RuleBasedNumberFormat(Locale.forLanguageTag("ru"),
				RuleBasedNumberFormat.SPELLOUT);
		nff.setMaximumFractionDigits(2);
		String[] monthNames = { "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь",
				"Октябрь", "Ноябрь", "Декабрь" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(client.getDateContract());
		String pattern = "dd.MM.yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		map.put("contractNumber", client.getContractNumber());
		map.put("city", client.getAppartment().getConstruction().getCity() == null ? "г. Бишкек"
				: client.getAppartment().getConstruction().getCity());
		map.put("dateContract", simpleDateFormat.format(client.getDateContract()));
		map.put("companyRegistration", client.getAppartment().getConstruction().getCompany().getLegalInfo());
		map.put("contractDay", cal.get(Calendar.DAY_OF_MONTH) + "");
		map.put("contractYear", cal.get(Calendar.YEAR) + "");
		map.put("contractMonth", monthNames[client.getDateContract().getMonth()]);
		map.put("companyTitle", client.getAppartment().getConstruction().getCompany().getTitle());
		map.put("longCompanyTitle", client.getAppartment().getConstruction().getCompany().getTitleLong());
		map.put("shortCompanyTitle", client.getAppartment().getConstruction().getCompany().getShortName());
		map.put("companyAddress", client.getAppartment().getConstruction().getCompany().getAddress());
		
		Long count=stService.countByProperty("client",client);
		if(count == null)count=0L;
		count++;
		map.put("contractModificationNumber",count.toString());
		map.put("procuration", client.getAppartment().getConstruction().getCompany().getProcuration());

		map.put("companyPostalAddress", client.getAppartment().getConstruction().getCompany().getPostAddress());
		map.put("companyOkpo", client.getAppartment().getConstruction().getCompany().getOkpo());
		map.put("companyInn", client.getAppartment().getConstruction().getCompany().getInn());
		map.put("companyBank", client.getAppartment().getConstruction().getCompany().getBankName());
		map.put("companyBik", client.getAppartment().getConstruction().getCompany().getBik());
		map.put("companyAccountNumber", client.getAppartment().getConstruction().getCompany().getAccountNumber());
		map.put("companyPhone", client.getAppartment().getConstruction().getCompany().getPhone());
		map.put("companySite", client.getAppartment().getConstruction().getCompany().getWebSite());
		map.put("companyResponsiblePersonIs",
				client.getAppartment().getConstruction().getCompany().getResponsiblePersonIs());
		map.put("companyRPShort", client.getAppartment().getConstruction().getCompany().getResponsiblePerson());

		map.put("clientFullName", client.getFio());
		if (client.getBirthdate() != null)
			map.put("clientBirthDate", simpleDateFormat.format(client.getBirthdate()));
		map.put("clientAddress", client.getPassportAddress());
		map.put("clientFactAddress", client.getFactAddress());
		
		map.put("clientOKPO", client.getFactAddress());
		map.put("clientINN", client.getPin());
		if (client.getBirthdate() != null)
			map.put("clientRegistrationDate", simpleDateFormat.format(client.getBirthdate()));
		map.put("clientDopTextContract", client.getExtraContractText());

		map.put("clientPin", client.getPin());
		map.put("clientPhone", client.getContacts());
		map.put("toForeman", client.getAppartment().getConstruction().getToForeman() == null ? ""
				: client.getAppartment().getConstruction().getToForeman());
		map.put("finDira", client.getAppartment().getConstruction().getCompany().getFinDirectorIs() == null ? ""
				: client.getAppartment().getConstruction().getCompany().getFinDirectorIs());
		switch (client.getAppartment().getType()) {
		case APPARTMENT:
			map.put("appartmentTypeR", "квартиры");
			break;
		case OFFICE:
			map.put("appartmentTypeR", "помещения");
			break;
		case PARKING:
			map.put("appartmentTypeR", "подземного машиноместа");
			break;		
		default:
			map.put("appartmentType", "");
		}
		map.put("today",simpleDateFormat.format(new Date()));
		map.put("finDir", client.getAppartment().getConstruction().getCompany().getFinDirector() == null ? ""
				: client.getAppartment().getConstruction().getCompany().getFinDirector());
		
		map.put("clientWhatsapp", client.getWhatsappNumber());

		if (client.getMaritalStatus() != true) {
			map.put("clientMarriageText1",
					"Я, " + client.getFio() + " настоящим заявляю, что в официальном браке не состою.");
			map.put("clientMarriageText2", "");

			map.put("cllineLong", "");
			map.put("clfio", "");
			map.put("cllineshort", "");
			map.put("clsign", "");
			map.put("cldate", "");
		} else {
			map.put("clientMarriageText1", "Я, " + client.getSpouseFio() + ", супруг/а " + client.getFio()
					+ " настоящим даю свое безусловное согласие супруге/у на заключение настоящего Договора/Соглашения.");
			map.put("clientMarriageText2", "Я, " + client.getFio() + ", супруг/а " + client.getSpouseFio()
					+ " обязуюсь обеспечить явку к вам свою/го супругу/а не позднее «___»________ 202____ г. для подписания вышеуказанного Заявления.");
			map.put("cllineLong", "______________________________________________________________");
			map.put("clfio", "(Ф.И.О.)");
			map.put("cllineshort", "________________________________");
			map.put("clsign", "(Подпись)");
			map.put("cldate", "(Дата)");
		}
		if (client.getContractType().equals(ContractType.VIA_STATE_REGISTER)) {
			map.put("competentnieOrgany", "В компетентные органы");
			map.put("otClientFrom", "От: " + client.getFio() + ",");
			if (client.getBirthdate() != null)
				map.put("textClientBirthdate", simpleDateFormat.format(client.getBirthdate()) + " года рождения,");
			map.put("otAddress", "проживающего по адресу: " + client.getFactAddress() + ",");
			map.put("otPassport", "паспорт № " + client.getPassportNumber());
			map.put("zayavlenie", "ЗАЯВЛЕНИЕ");
			String ap = "квартиры";
			String ending = "й";
			if (client.getAppartment().getType().equals(AppartmentType.OFFICE)) {
				if (client.getAppartment().getOfficeType().equals(OfficeType.BASEMENT))
					ap = "Нежилого помещения";
				else if (client.getAppartment().getOfficeType().equals(OfficeType.GARRET))
					ap = "Нежилого помещения";
				else
					ap = "помещения";

				ending = "го";
			} else if (client.getAppartment().getType().equals(AppartmentType.PARKING)) {
				ap = "подземного Машиономеста";
				ending = "го";
			} else if (client.getAppartment().getConstruction().isCottage() == true
					&& client.getAppartment().getConstruction().getSpecialType() == null) {
				ap = "жилого дома (коттеджа)";
				ending = "го";
			} else if (client.getAppartment().getConstruction().getSpecialType() != null
					&& client.getAppartment().getConstruction().getSpecialType().equals(4)) {
				ap = "квартиры";
				ending = "й";
			}

			if (client.getBirthdate() != null)
				map.put("textZayavleniya", "Я, " + client.getFio() + ", "
						+ simpleDateFormat.format(client.getBirthdate())
						+ " года рождения, являясь приобретателем имущества по предварительному договору купли-продажи "
						+ ap + ", расположенно" + ending + " по адресу: "
						+ client.getAppartment().getConstruction().getPlannedAddress() + ", идентификационный код № "
						+ client.getAppartment().getConstruction().getCode() + ", настоящим даю свое согласие "
						+ client.getAppartment().getConstruction().getCompany().getTitle()
						+ ", на проведение землеустроительных работ, инвентаризацию, регистрацию акта приемки в эксплуатацию, получение технических паспортов, в случае необходимости включения дополнительных застройщиков, объединения земельных участков и проведения всех необходимых работ связанных с вводом в эксплуатацию многоквартирных жилых домов по вышеуказанному адресу, по своему усмотрению.");
			map.put("2TextZayavleniya",
					"Текст заявления прочитано мной до подписания и соответствует моим действительным намерениям.");
		} else {
			map.put("competentnieOrgany", "");
			map.put("otClientFrom", "");
			map.put("textClientBirthdate", "");
			map.put("otAddress", "");
			map.put("otPassport", "");
			map.put("zayavlenie", "");
			map.put("textZayavleniya", "");
			map.put("2TextZayavleniya", "");
		}
		map.put("clientPassport", client.getPassportNumber());
		map.put("crmNumber", client.getCrmNumber() == null ? "" : client.getCrmNumber());
		map.put("extraLow", (client.getExtralow()==true)?"Ниже сетки!!!":"");
		
		List<FilterExample> exs = new ArrayList<FilterExample>();
		exs.add(new FilterExample("client", client, InequalityConstants.EQUAL));
		Date mx=new Date();
		for(Schedule s:sService.findByExample(0, 1, SortEnum.DESCENDING, exs, "datePayment")) {
            if(s.getDatePayment() != null) {
                mx= s.getDatePayment();
            }
        }
		long daysDifference = getDaysDifference(client.getAppartment().getConstruction().getInstallmentDate(), mx);
		map.put("installmentNote", (daysDifference>0)?"Продлено на "+daysDifference+" день/дней/дня":"");
		map.put("contractType",Messages.getEnumMessage(client.getType().toString()));
		map.put("clientType",Messages.getEnumMessage(client.getClientFrom().toString()));
		map.put("clientFrom", client.getClientFrom().equals(ClientFrom.OTHERS)?client.getFromPerson().getFio():"-");
		map.put("curatorOp", client.getCurator() == null ? "-" : client.getCurator().getFio());
		map.put("curatorOrk", client.getCuratorOrk() == null ? "-" : client.getCuratorOrk().getFio());
		//map.put("curator", client.getCurator() == null ? "-" : client.getCurator().getFio());
		
		map.put("installmentDate",simpleDateFormat.format(mx));
		map.put("contractDate",simpleDateFormat.format(client.getDateContract()));
		map.put("note",client.getNote() == null ? "" : client.getNote());
		
		map.put("signPerson", client.getAppartment().getConstruction().getCompany().getPsdFio());
		map.put("responsibleFrom", client.getClientFrom().equals(ClientFrom.OTHERS) ? client.getFromPerson().getFio()+" _________________________"
				:"");
		map.put("constructionAddress", client.getAppartment().getConstruction().getPlannedAddress());
		map.put("constructionDocs", client.getAppartment().getConstruction().getLegalDocuments());
		map.put("constructionEni", client.getAppartment().getConstruction().getCode());

		map.put("constructionName", client.getAppartment().getConstruction().getTitle());
		map.put("constructionSmr", simpleDateFormat.format(client.getAppartment().getConstruction().getPlannedDate()));
		map.put("constructionSve", simpleDateFormat.format(client.getAppartment().getConstruction().getRealDate()));
		map.put("appartmentNumber", client.getAppartment().getTitle());
		if (client.getAppartment().getDocNumber() != null && client.getAppartment().getDocNumber().length() > 0)
			map.put("appartmentNumber", client.getAppartment().getTitle() + " (После инвент.: "
					+ client.getAppartment().getDocNumber() + ")");
		map.put("appartmentTotalArea", client.getAppartment().getTotalArea() + "");
		if (client.getAppartment().getDocTotalArea() != null)
			map.put("appartmentTotalArea", client.getAppartment().getTotalArea() + " (После инвент.: "
					+ client.getAppartment().getDocTotalArea() + ")");
		map.put("appartmentFlat", client.getAppartment().getFlat() + "");
		map.put("appartmentBlock", client.getAppartment().getBlockNumber() + "");
		map.put("appartmentEntrance", client.getAppartment().getEntranceNumber() + "");
		map.put("appartmentApEntrance", client.getAppartment().getAppartmentEntranceNumber() + "");

		map.put("appartmentRoomQuantity", client.getAppartment().getRoomQuantity() + "");

		String ttotalsum = client.getTotalSum().multiply(BigDecimal.valueOf(100l)).remainder(BigDecimal.valueOf(100l))
				.intValue() + "";
		if (ttotalsum.length() < 2) {
			ttotalsum = "0" + ttotalsum;
		}

		map.put("contractSumDolText",
				nff.format(client.getTotalSum().setScale(0, BigDecimal.ROUND_FLOOR)).trim() + " "
						+ client.getAppartment().getConstruction().getCompany().getCurrency() + " " + ttotalsum + " "
						+ client.getAppartment().getConstruction().getCompany().getSubCurrency());
		map.put("contractSumSomText",
				nff.format(client.getTotalSum().setScale(0, BigDecimal.ROUND_FLOOR)).trim() + " "
						+ client.getAppartment().getConstruction().getCompany().getCurrency() + " " + ttotalsum + " "
						+ client.getAppartment().getConstruction().getCompany().getSubCurrency());

		if (client.getContractType().equals(ContractType.VIA_MAIN)) {
			map.put("contractQuantity", "2");
			map.put("contract711", "");
		} else if (client.getContractType().equals(ContractType.VIA_NOTARY)) {
			map.put("contractQuantity", "3");
			map.put("contract711", " и нотариальной конторе");
		} else {
			map.put("contractQuantity", "4");
			map.put("contract711",
					", нотариальной конторе и территориальному органу по регистрации прав на недвижимое имущество");
		}

		ttotalsum = client.getFirstPayment().multiply(BigDecimal.valueOf(100l)).remainder(BigDecimal.valueOf(100l))
				.intValue() + "";
		if (ttotalsum.length() < 2) {
			ttotalsum = "0" + ttotalsum;
		}
		map.put("firstPaymentSumSomText",
				nff.format(client.getFirstPayment().setScale(0, BigDecimal.ROUND_FLOOR)).trim() + " "
						+ client.getAppartment().getConstruction().getCompany().getCurrency() + " " + ttotalsum + " "
						+ client.getAppartment().getConstruction().getCompany().getSubCurrency());
		ttotalsum = client.getPriceForSquare().multiply(BigDecimal.valueOf(100l)).remainder(BigDecimal.valueOf(100l))
				.intValue() + "";
		if (ttotalsum.length() < 2) {
			ttotalsum = "0" + ttotalsum;
		}
		map.put("contractPerSquareText",
				nff.format(client.getPriceForSquare().setScale(0, BigDecimal.ROUND_FLOOR)).trim() + " "
						+ client.getAppartment().getConstruction().getCompany().getCurrency() + " " + ttotalsum + " "
						+ client.getAppartment().getConstruction().getCompany().getSubCurrency());

		map.put("contractSumDolNum", client.getTotalSum() + "");
		map.put("contractSumSomNum", client.getTotalSum() + "");
		exs=new ArrayList<FilterExample>();
		exs.add(new FilterExample("client", client, InequalityConstants.EQUAL));
		exs.add(new FilterExample("paymentType", PaymentType.DISCOUNT, InequalityConstants.EQUAL));
		BigDecimal discount = BigDecimal.ZERO;
		for (Payment p : pService.findByExample(0, 100, exs)) {
			discount = discount.add(p.getPaymentAmount());
		}
		
		if (client.getAppartment().getDocTotalArea() != null) {
			map.put("contractSumFact", client.getAppartment().getDocTotalArea().multiply(client.getPriceForSquare())
					.setScale(2, BigDecimal.ROUND_FLOOR) + "");
			map.put("contractSumSignDoc", client.getAppartment().getDocTotalArea().multiply(client.getPriceForSquare())
					.setScale(2, BigDecimal.ROUND_FLOOR).subtract(discount) + "");
		} else {
			map.put("contractSumFact", client.getTotalSum() + "");
			map.put("contractSumSignDoc", client.getTotalSum().subtract(discount) + "");
		}
		
		map.put("contractPerSquareNum", client.getPriceForSquare() + "");
		map.put("firstPaymentSumSomNum", client.getFirstPayment() + "");
		map.put("constructionIdentificationNumber", client.getAppartment().getConstruction().getCode());
		map.put("clientInn", client.getPin());
		map.put("toLawyer", client.getAppartment().getConstruction().getCompany().getToLawyer() == null ? ""
				: client.getAppartment().getConstruction().getCompany().getToLawyer());
		map.put("clientDetails", client.getFio() + "\n Test /n Test \r sadasd /t sadsadas ");
		return map;
	}
	public static long getDaysDifference(Date startDate, Date endDate) {
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return TimeUnit.MILLISECONDS.toDays(diffInMillis);
    }

	public DefaultStreamedContent generateClientContractPassport(Client client) {
		Map<String, String> map = prepareClientDetailsForContract(client);

		InputStream inputStream = null;
		inputStream = getDocumentTemplate(DocumentTemplateType.PASSPORT_DEAL, inputStream);

		List<FilterExample> examples = new ArrayList<FilterExample>();
		examples.add(new FilterExample("client", client, InequalityConstants.EQUAL));		
		if (inputStream == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"На данный тип документа : \"" + Messages.getEnumMessage(DocumentTemplateType.PASSPORT_DEAL.toString()) 
									+ "\" - нет Шаблона документа! ",
							null));
			return null;
		}
		

		InputStream stream = null;
		try {
			XSSFWorkbook doc = docController.replaceTextXlsx(inputStream, map);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			doc.write(byteArrayOutputStream);
			doc.close();
			stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return new DefaultStreamedContent(stream, externalContext.getMimeType("excel.xlsx"),
				Translit.translit(client.getContractNumber()) + ".xlsx");
	}
	public DefaultStreamedContent generateClientContractNew(Client client) {
		Map<String, String> map = prepareClientDetailsForContract(client);

		InputStream inputStream = null;
		inputStream = getContractTemplate(client, inputStream);
		
		List<FilterExample> examples = new ArrayList<FilterExample>();
		examples.add(new FilterExample("client", client, InequalityConstants.EQUAL));
		List<Schedule> schedules = sService.findByExample(0, 100, SortEnum.ASCENDING, examples, "datePayment");
		Map<String, List<Schedule>> mp = new HashMap<String, List<Schedule>>();
		mp.put("paymentSchedule", schedules);

		Set<String> equipment = client.getAppartment().getConstruction().getFields();
		Map<String, Set<String>> mps = new HashMap<String, Set<String>>();
		mps.put("constructionEquipment", equipment);

		if (inputStream == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"На данный тип объекта : \"" + client.getAppartment().getConstruction().getTitle() + " : "
									+ Messages.getEnumMessage(client.getAppartment().getType().toString())+ " : "
											+ Messages.getEnumMessage(client.getFizYur().toString())
									+ "\" - нет Шаблона договора! ",
							null));
			return null;
		}

		InputStream stream = null;
		try {
			XWPFDocument doc = docController.replaceTextDocx(inputStream, map, mp, mps);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			doc.write(byteArrayOutputStream);
			doc.close();
			stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
		} catch (Exception e) {
			XWPFDocument doc = new XWPFDocument();
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				XWPFParagraph p = doc.createParagraph();
				XWPFRun r = p.createRun();
				r.setText("Шаблон договора на объект " + client.getAppartment().getConstruction().getTitle() + " : "
						+ Messages.getEnumMessage(client.getAppartment().getType().toString()) + " не корректный!!!",
						0);
				r.setFontFamily("Times New Roman");
				r.setFontSize(24);
				r.addBreak();

				doc.write(byteArrayOutputStream);
				doc.close();
				stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
				byteArrayOutputStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// TODO Auto-generated catch block
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return new DefaultStreamedContent(stream, externalContext.getMimeType("bank.docx"),
				Translit.translit(client.getContractNumber()) + ".docx");
	}

	private InputStream getContractTemplate(Client client, InputStream inputStream) {
		List<FilterExample> examples = new ArrayList<FilterExample>();
		examples.add(new FilterExample("construction", client.getAppartment().getConstruction(),
				InequalityConstants.EQUAL));
		examples.add(
				new FilterExample("fizYur", client.getFizYur(), InequalityConstants.EQUAL));
		if (client.getAppartment().getType().equals(AppartmentType.APPARTMENT)) {
			examples.add(new FilterExample("type", ContractTemplateType.APPARTMENT, InequalityConstants.EQUAL));
		}else if (client.getAppartment().getType().equals(AppartmentType.OFFICE)) {
			if (client.getAppartment().getOfficeType().equals(OfficeType.BASEMENT))
				examples.add(new FilterExample("type", ContractTemplateType.BASEMENT, InequalityConstants.EQUAL));
			else if (client.getAppartment().getOfficeType().equals(OfficeType.GARRET))
				examples.add(new FilterExample("type", ContractTemplateType.GARRET, InequalityConstants.EQUAL));
			else
				examples.add(new FilterExample("type", ContractTemplateType.OFFICE, InequalityConstants.EQUAL));

		} else if (client.getAppartment().getType().equals(AppartmentType.PARKING)) {
			examples.add(new FilterExample("type", ContractTemplateType.PARKING, InequalityConstants.EQUAL));
		}
		
		
		for (ContractTemplate ct : ctService.findByExample(0, 100, examples)) {
			inputStream = new ByteArrayInputStream(ct.getAttachment().getData());
		}
		return inputStream;
	}

	private InputStream getDocumentTemplate(DocumentTemplateType type, InputStream inputStream) {
		List<FilterExample> examples = new ArrayList<FilterExample>();
		examples.add(new FilterExample("type", type, InequalityConstants.EQUAL));
		for (DocumentTemplate dt : dtService.findByExample(0, 100, examples)) {
			inputStream = new ByteArrayInputStream(dt.getAttachment().getData());
		}
		return inputStream;
	}
	
	public DefaultStreamedContent generateClientContractDirectNew(Client client) {
		Map<String, String> map = prepareClientDetailsForContract(client);

		InputStream inputStream = null;
		inputStream = getContractTemplate(client, inputStream);
		if (inputStream == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"На данный тип объекта : \"" + client.getAppartment().getConstruction().getTitle() + " : "
									+ Messages.getEnumMessage(client.getAppartment().getType().toString())+ " : "
											+ Messages.getEnumMessage(client.getFizYur().toString())
									+ "\" - нет Шаблона договора! ",
							null));
			return null;
		}

		List<FilterExample> examples = new ArrayList<FilterExample>();
		examples.add(new FilterExample("client", client, InequalityConstants.EQUAL));
		List<Schedule> schedules = sService.findByExample(0, 100, SortEnum.ASCENDING, examples, "datePayment");
		Map<String, List<Schedule>> mp = new HashMap<String, List<Schedule>>();
		mp.put("paymentSchedule", schedules);

		Set<String> equipment = client.getAppartment().getConstruction().getFields();
		Map<String, Set<String>> mps = new HashMap<String, Set<String>>();
		mps.put("constructionEquipment", equipment);

		InputStream stream = null;
		try {
			XWPFDocument doc = docController.replaceTextDocx(inputStream, map, mp, mps);
			doc.enforceReadonlyProtection("changeOnly", HashAlgorithm.md5);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			doc.write(byteArrayOutputStream);
			doc.close();
			stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return new DefaultStreamedContent(stream, externalContext.getMimeType("bank.docx"),
				Translit.translit(client.getContractNumber()) + ".docx");
	}

	public void generateClientContract(Client client) {
		System.out.println("generateContract");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		map.put("client_id", client.getId());

		System.out.println("=== journalViewReport ===");
		System.out.println("client_id" + client);
		System.out.println("sp-type=" + client.getAppartment().getConstruction().getSpecialType());

		JasperPrint jasperPrint = null;
		try {
			if (client.getAppartment().getType().equals(AppartmentType.APPARTMENT)) {
				if (client.getAppartment().getConstruction().isCottage() == true)
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_resort.jasper");
				else if (client.getAppartment().getConstruction().getSpecialType() != null
						&& client.getAppartment().getConstruction().getSpecialType().equals(4))
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_resort_ap.jasper");
				else
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template.jasper");
			}
			if (client.getAppartment().getType().equals(AppartmentType.OFFICE)) {
				if (client.getAppartment().getOfficeType().equals(OfficeType.BASEMENT))
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_basement.jasper");
				else if (client.getAppartment().getOfficeType().equals(OfficeType.GARRET))
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_garret.jasper");
				else
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_office.jasper");

			}
			if (client.getAppartment().getType().equals(AppartmentType.PARKING))
				jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_parking.jasper");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		jasperViewerController.view(jasperPrint);
	}

	public DefaultStreamedContent generateClientContractDirect(Client client) throws IOException {
		System.out.println("generateContract");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		map.put("client_id", client.getId());

		System.out.println("=== journalViewReport ===");
		System.out.println("client_id" + client);

		JasperPrint jasperPrint = null;
		try {
			if (client.getAppartment().getType().equals(AppartmentType.APPARTMENT)) {
				if (client.getAppartment().getConstruction().isCottage() == true)
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_resort.jasper");
				else if (client.getAppartment().getConstruction().getSpecialType() != null
						&& client.getAppartment().getConstruction().getSpecialType().equals(4))
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_resort_ap.jasper");
				else
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template.jasper");
			}
			if (client.getAppartment().getType().equals(AppartmentType.OFFICE)) {
				if (client.getAppartment().getOfficeType().equals(OfficeType.BASEMENT))
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_basement.jasper");
				else if (client.getAppartment().getOfficeType().equals(OfficeType.GARRET))
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_garret.jasper");
				else
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_office.jasper");

			}
			if (client.getAppartment().getType().equals(AppartmentType.PARKING))
				jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_parking.jasper");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

		if (client.getContractType().equals(ContractType.VIA_STATE_REGISTER)) {
			PDFMergerUtility ut = new PDFMergerUtility();
			ut.addSource(getPDFStream(jasperPrint));
			JasperPrint jasperPrint2 = null;
			try {
				jasperPrint2 = generateJasperPrint(map, ds.getConnection(), "state_register_claim.jasper");

			} catch (SQLException e) {
				e.printStackTrace();
			}
			ut.addSource(getPDFStream(jasperPrint2));

			ByteArrayOutputStream destStream = new ByteArrayOutputStream();
			ut.setDestinationStream(destStream);
			ut.mergeDocuments();
			InputStream is = new ByteArrayInputStream(destStream.toByteArray());
			return new DefaultStreamedContent(is, externalContext.getMimeType("word.pdf"),
					Translit.translit(client.getContractNumber()) + ".pdf");

		} else
			return new DefaultStreamedContent(getPDFStream(jasperPrint), externalContext.getMimeType("word.pdf"),
					Translit.translit(client.getContractNumber()) + ".pdf");
	}

	public DefaultStreamedContent generateClientReserveDirect(Client client) {
		System.out.println("generateContract");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		map.put("client_id", client.getId());

		System.out.println("=== journalViewReport ===");
		System.out.println("client_id" + client);

		JasperPrint jasperPrint = null;
		try {
			if (client.getAppartment().getType().equals(AppartmentType.APPARTMENT)) {
				if (client.getAppartment().getConstruction().isCottage() == true)
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_resort.jasper");
				else if (client.getAppartment().getConstruction().getSpecialType() != null
						&& client.getAppartment().getConstruction().getSpecialType().equals(4))
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_resort_ap.jasper");
				else
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template.jasper");
			}
			if (client.getAppartment().getType().equals(AppartmentType.OFFICE)) {
				if (client.getAppartment().getOfficeType().equals(OfficeType.BASEMENT))
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_basement.jasper");
				else if (client.getAppartment().getOfficeType().equals(OfficeType.GARRET))
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_garret.jasper");
				else
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_office.jasper");

			}
			if (client.getAppartment().getType().equals(AppartmentType.PARKING))
				jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_parking.jasper");

			jasperPrint = generateJasperPrint(map, ds.getConnection(), "reserve_template.jasper");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return new DefaultStreamedContent(getPDFStream(jasperPrint), externalContext.getMimeType("word.pdf"),
				Translit.translit(client.getContractNumber()) + ".pdf");
	}

	public DefaultStreamedContent generateClientBankDirect(Client client) {
		System.out.println("generateBank");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		map.put("client_id", client.getId());

		System.out.println("=== journalViewReport ===");
		System.out.println("client_id" + client);

		JasperPrint jasperPrint = null;
		try {
			jasperPrint = generateJasperPrint(map, ds.getConnection(), "bank_payment.jasper");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return new DefaultStreamedContent(getPDFStream(jasperPrint), externalContext.getMimeType("bank.pdf"),
				"bank_" + Translit.translit(client.getContractNumber()) + ".pdf");
	}

	public DefaultStreamedContent generateClientGotKeys(Client client) {

		List<FilterExample> examples = new ArrayList<>();
		examples.add(new FilterExample("client", client, InequalityConstants.EQUAL));
		examples.add(new FilterExample("datePayment", new Date(), InequalityConstants.LESSER_OR_EQUAL));
		for (Schedule s : sService.findByExample(0, 1000, examples)) {
			if (s.getActive() == null || s.getActive().equals(false)) {
				Boolean show = true;
				if (client.getAppartment().getDocTotalArea() != null) {
					if (client.getAlreadyPayed().add(new BigDecimal(1)).compareTo(
							client.getAppartment().getDocTotalArea().multiply(client.getPriceForSquare())) >= 0)
						show = false;
				}
				if (show) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Клиент не оплатил все платежи по графику!!!",
									"Клиент не оплатил все платежи по графику!!!"));
					return null;
				}
			}
		}

		if (sService.countByExample(examples) < 1) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Клиент не оплатил все платежи по графику!!!", "Клиент не оплатил все платежи по графику!!!"));
			return null;
		}

		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		map.put("client_id", client.getId());
		JasperPrint jasperPrint = null;
		DocumentTemplateType docType= DocumentTemplateType.KEYS_DOCUMENT;
		Boolean debtor = false;

		System.out.println("client.getNotPayedYet()=" + client.getNotPayedYet());
		if (client.getNotPayedYet().compareTo(BigDecimal.ZERO) == 1) {
			if (client.getAppartment().getDocTotalArea() != null) {
				if (client.getAppartment().getDocTotalArea().multiply(client.getPriceForSquare())
						.compareTo(client.getAlreadyPayed()) > 0)
					debtor = true;
			} else
				debtor = true;

		}
		if (debtor == false) {
			if (client.getAppartment().getDocTotalArea() != null) {
				if (client.getAppartment().getDocTotalArea().multiply(client.getPriceForSquare())
						.compareTo(client.getAlreadyPayed()) > 0)
					debtor = true;
			}
		}

		if (debtor)
			docType = DocumentTemplateType.KEYS_DOCUMENT_DEBT;				
		else
			docType = DocumentTemplateType.KEYS_DOCUMENT;

		Client c = service.findById(client.getId(), false);
		c.setKeys(true);
		service.merge(c);

		InputStream inputStream = null;
		inputStream = getDocumentTemplate(docType, inputStream);
		
		if (inputStream == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"На данный тип документа : \"" + Messages.getEnumMessage(docType.toString()) 
									+ "\" - нет Шаблона документа! ",
							null));
			return null;
		}
		
		Map<String, String> map2 = prepareClientDetailsForContract(client);
		InputStream stream = null;
		try {
			XWPFDocument doc = docController.replaceTextDocx(inputStream, map2, new HashMap<String, List<Schedule>>(),new HashMap<String, Set<String>>());
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			doc.write(byteArrayOutputStream);
			doc.close();
			stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return new DefaultStreamedContent(stream, externalContext.getMimeType("bank.docx"),
				Translit.translit(client.getContractNumber()) + ".docx");
		
		

	}

	public DefaultStreamedContent generateClientSigned(Client client) {

		if (!client.getAppartment().getType().equals(AppartmentType.PARKING)
				&& client.getAppartment().getDocTotalArea() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Введите площадь по техпаспорту!!!", "Введите площадь по техпаспорту!!!"));
			return null;
		}

		if (!client.getAppartment().getType().equals(AppartmentType.PARKING)
				&& (client.getAppartment().getDocNumber() == null
						|| client.getAppartment().getDocNumber().length() < 1)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Введите номер по техпаспорту!!!", "Введите номер по техпаспорту!!!"));
			return null;
		}

		if (client.getAlreadyPayed() == null || client.getAlreadyPayed().compareTo(new BigDecimal(1)) < 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Клиент полностью не погасил сумму договора!!!", "Клиент полностью не погасил сумму договора!!!"));
			return null;
		}

		if (client.getAppartment().getType().equals(AppartmentType.PARKING))
			client.getAppartment().setDocTotalArea(new BigDecimal(1));
		if (client.getAppartment().getDocTotalArea().multiply(client.getPriceForSquare())
				.compareTo(client.getAlreadyPayed().add(new BigDecimal(1))) > 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Клиент полностью не погасил сумму договора!!!", "Клиент полностью не погасил сумму договора!!!"));
			return null;
		}

		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		map.put("client_id", client.getId());
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = generateJasperPrint(map, ds.getConnection(), "sign_report.jasper");
			if (client.getType().equals(ClientType.SURETY)) {
				jasperPrint = generateJasperPrint(map, ds.getConnection(), "sign_report_pledge.jasper");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		Client c = service.findById(client.getId(), false);
		c.setSigned(true);
		service.merge(c);
		
		InputStream inputStream = null;
		inputStream = getDocumentTemplate(DocumentTemplateType.SIGN_DOCUMENT, inputStream);
		
		if (inputStream == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"На данный тип документа : \"" + Messages.getEnumMessage(DocumentTemplateType.SIGN_DOCUMENT.toString()) 
									+ "\" - нет Шаблона документа! ",
							null));
			return null;
		}
		
		Map<String, String> map2 = prepareClientDetailsForContract(client);
		InputStream stream = null;
		try {
			XWPFDocument doc = docController.replaceTextDocx(inputStream, map2, new HashMap<String, List<Schedule>>(),new HashMap<String, Set<String>>());
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			doc.write(byteArrayOutputStream);
			doc.close();
			stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return new DefaultStreamedContent(stream, externalContext.getMimeType("bank.docx"),
				Translit.translit("signed_" + client.getFio()) + ".docx");
		
		
	}

	public DefaultStreamedContent generateClientContractDirectWord(Client client) {
		System.out.println("generateContract");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		map.put("client_id", client.getId());

		System.out.println("=== journalViewReport ===");
		System.out.println("client_id" + client);

		JasperPrint jasperPrint = null;
		try {
			if (client.getAppartment().getType().equals(AppartmentType.APPARTMENT)) {
				if (client.getAppartment().getConstruction().isCottage() == true)
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_resort.jasper");
				else if (client.getAppartment().getConstruction().getSpecialType() != null
						&& client.getAppartment().getConstruction().getSpecialType().equals(4))
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_resort_ap.jasper");
				else
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template.jasper");
			}
			if (client.getAppartment().getType().equals(AppartmentType.OFFICE)) {
				if (client.getAppartment().getOfficeType().equals(OfficeType.BASEMENT))
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_basement.jasper");
				else if (client.getAppartment().getOfficeType().equals(OfficeType.GARRET))
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_garret.jasper");
				else
					jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_office.jasper");

			}
			if (client.getAppartment().getType().equals(AppartmentType.PARKING))
				jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_template_parking.jasper");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return new DefaultStreamedContent(getWordStream(jasperPrint), externalContext.getMimeType("word.docx"),
				Translit.translit(client.getContractNumber()) + ".docx");
	}

	public DefaultStreamedContent generateClientGroundWork(Client client) {
		System.out.println("generateContract");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		map.put("client_id", client.getId());

		System.out.println("=== journalViewReport ===");
		System.out.println("client_id" + client);

		JasperPrint jasperPrint = null;
		try {
			jasperPrint = generateJasperPrint(map, ds.getConnection(), "construction_agreement.jasper");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return new DefaultStreamedContent(getWordStream(jasperPrint), externalContext.getMimeType("word.docx"),
				Translit.translit(client.getContractNumber()) + ".docx");
	}

	public DefaultStreamedContent generateClientContractPassportOld(Client client) {
		System.out.println("generateContract");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		map.put("client_id", client.getId());

		System.out.println("=== journalViewReport ===");
		System.out.println("client_id" + client);

		JasperPrint jasperPrint = null;
		try {
			// if(client.getAppartment().getType().equals(AppartmentType.APPARTMENT))
			jasperPrint = generateJasperPrint(map, ds.getConnection(), "contract_passport.jasper");
			/*
			 * if(client.getAppartment().getType().equals(AppartmentType.OFFICE))
			 * jasperPrint = generateJasperPrint(map, ds.getConnection(),
			 * "contract_template_office.jasper");
			 * if(client.getAppartment().getType().equals(AppartmentType.PARKING))
			 * jasperPrint = generateJasperPrint(map, ds.getConnection(),
			 * "contract_template_parking.jasper");
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return new DefaultStreamedContent(getExcelStream(jasperPrint), externalContext.getMimeType("excel.xls"),
				Translit.translit(client.getContractNumber()) + ".xls");
	}

	public DefaultStreamedContent generateDenounceApplication(Denounce denounce) {
		InputStream inputStream = null;
		inputStream = getDocumentTemplate(DocumentTemplateType.DENOUNCE_APPLICATION, inputStream);
		
		if (inputStream == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"На данный тип документа : \"" + Messages.getEnumMessage(DocumentTemplateType.DENOUNCE_APPLICATION.toString()) 
									+ "\" - нет Шаблона документа! ",
							null));
			return null;
		}
		
		Map<String, String> map2 = prepareClientDetailsForContract(denounce.getAppartment().getClient());
		String pattern = "dd.MM.yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		map2.put("denounceDateAgreement", simpleDateFormat.format(denounce.getDateDenounce()));
		map2.put("denounceDateCreated", simpleDateFormat.format(denounce.getDateCreated()));
		
		
		InputStream stream = null;
		try {
			XWPFDocument doc = docController.replaceTextDocx(inputStream, map2, new HashMap<String, List<Schedule>>(),new HashMap<String, Set<String>>());
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			doc.write(byteArrayOutputStream);
			doc.close();
			stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return new DefaultStreamedContent(stream, externalContext.getMimeType("bank.docx"),
				Translit.translit("DAP_" + denounce.getAppartment().getClient().getFio()) + ".docx");
	}

	public DefaultStreamedContent generateDenounceAgreement(Denounce denounce) {
		InputStream inputStream = null;
		inputStream = getDocumentTemplate(DocumentTemplateType.DENOUNCE_AGREEMENT, inputStream);
		
		if (inputStream == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"На данный тип документа : \"" + Messages.getEnumMessage(DocumentTemplateType.DENOUNCE_AGREEMENT.toString()) 
									+ "\" - нет Шаблона документа! ",
							null));
			return null;
		}
		
		Map<String, String> map2 = prepareClientDetailsForContract(denounce.getAppartment().getClient());
		String pattern = "dd.MM.yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		map2.put("denounceDateAgreement", simpleDateFormat.format(denounce.getDateDenounce()));
		map2.put("denounceDateCreated", simpleDateFormat.format(denounce.getDateCreated()));
		
		
		InputStream stream = null;
		try {
			XWPFDocument doc = docController.replaceTextDocx(inputStream, map2, new HashMap<String, List<Schedule>>(),new HashMap<String, Set<String>>());
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			doc.write(byteArrayOutputStream);
			doc.close();
			stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return new DefaultStreamedContent(stream, externalContext.getMimeType("bank.docx"),
				Translit.translit("DAG_" + denounce.getAppartment().getClient().getFio()) + ".docx");
	}

	public DefaultStreamedContent generateScheduleModifyAgreement(ScheduleTemplate sch) {
		System.out.println("generateContract");
		DocumentTemplateType docType = DocumentTemplateType.SCHEDULE_TEMPLATE;
		InputStream inputStream = null;
		inputStream = getDocumentTemplate(docType, inputStream);
		
		if (inputStream == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"На данный тип документа : \"" + Messages.getEnumMessage(docType.toString()) 
									+ "\" - нет Шаблона документа! ",
							null));
			return null;
		}
		
		List<FilterExample> examples = new ArrayList<FilterExample>();
		examples.add(new FilterExample("scheduleTemplate", sch, InequalityConstants.EQUAL));
		List<SubSchedule> schedules = ssService.findByExample(0, 100, SortEnum.ASCENDING, examples, "datePayment");
		Map<String, List<SubSchedule>> mp = new HashMap<String, List<SubSchedule>>();
		mp.put("paymentSchedule", schedules);
		
		
		
		Map<String, String> map2 = prepareClientDetailsForContract(sch.getClient());
		
		List<FilterExample> examples2 = new ArrayList<FilterExample>();
		examples2.add(new FilterExample("id", sch.getId(), InequalityConstants.LESSER_OR_EQUAL));
		examples2.add(new FilterExample("client", sch.getClient(), InequalityConstants.EQUAL));
		map2.put("dopNumber", stService.countByExample(examples2) + "");
		
		InputStream stream = null;
		try {
			XWPFDocument doc = docController.replaceTextDocxS(inputStream, map2, mp,new HashMap<String, Set<String>>());
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			doc.write(byteArrayOutputStream);
			doc.close();
			stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return new DefaultStreamedContent(stream, externalContext.getMimeType("bank.docx"),
				Translit.translit("dop_"+sch.getClient().getContractNumber()) + ".docx");		
	}

	private InputStream getPDFStream(JasperPrint jasperPrint) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		List<JasperPrint> jasperPrintList = new ArrayList<>();
		jasperPrintList.add(jasperPrint);

		JRPdfExporter exporter = new JRPdfExporter();

		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
		configuration.setCreatingBatchModeBookmarks(true);
		exporter.setConfiguration(configuration);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
		}

		try {
			InputStream stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
			return stream;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private InputStream getWordStream(JasperPrint jasperPrint) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		List<JasperPrint> jasperPrintList = new ArrayList<>();
		jasperPrintList.add(jasperPrint);

		JRDocxExporter exporter = new JRDocxExporter();

		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
		SimpleDocxExporterConfiguration configuration = new SimpleDocxExporterConfiguration();
		exporter.setConfiguration(configuration);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
		}

		try {
			InputStream stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
			return stream;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private InputStream getExcelStream(JasperPrint jasperPrint) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		List<JasperPrint> jasperPrintList = new ArrayList<>();
		jasperPrintList.add(jasperPrint);

		JRXlsExporter exporter = new JRXlsExporter();

		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
		SimpleXlsExporterConfiguration configuration = new SimpleXlsExporterConfiguration();
		exporter.setConfiguration(configuration);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
		}

		try {
			InputStream stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
			return stream;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean isValue1() {
		return value1;
	}

	public void setValue1(boolean value1) {
		this.value1 = value1;
	}

	public void addMessage() {
		String summary = value1 ? "Checked" : "Unchecked";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
	}

}
