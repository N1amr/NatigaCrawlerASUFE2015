import java.util.ArrayList;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;

public class Natega implements Comparable<Natega> {

    private String name = null;
    private String studentCode = null;
    private int seatNumber = 0;
    private int grade = 0;
    private String department = null;
    private String spec = null;
    private String ta2deer = null;
    private int total = 0;

    public static final String FIELD_NAME = "name";
    public static final String FIELD_STUDENT_CODE = "code";
    public static final String FIELD_SEAT_NUMBER = "seat.number";
    public static final String FIELD_GRADE = "grade";
    public static final String FIELD_DEPARTMENT = "department";
    public static final String FIELD_SPEC = "spec";
    public static final String FIELD_TA2DEER = "ta2deer";
    public static final String FIELD_TOTAL = "total";

    public static Natega fromXML(String xmlText) {
	Natega natega = new Natega();
	try {
	    Document document = DocumentHelper.parseText(xmlText);

	    Element root = document.getRootElement();
	    Element body = root.element("body");
	    Element form = body.element("form");
	    Element div = (Element) form.node(5);
	    Element center = (Element) div.elements("center").get(0);
	    Element h2 = (Element) center.elements("h2").get(0);

	    ArrayList<String> elementsValue = new ArrayList<String>();

	    for (Object obj : h2.elements())
		if (obj instanceof Element) {
		    Element element = (Element) obj;
		    elementsValue.add(element.getText().trim());
		}

	    natega.name = elementsValue.get(0).trim().substring(13);

	    natega.studentCode = (elementsValue.get(1).substring(13));
	    natega.seatNumber = Integer.valueOf(elementsValue.get(2).substring(13));
	    natega.grade = Integer.valueOf(elementsValue.get(3).substring(9));
	    natega.department = elementsValue.get(4).substring(9);
	    natega.spec = elementsValue.get(5).substring(9);
	    natega.ta2deer = elementsValue.get(6).substring(16);
	    natega.total = Integer.valueOf(elementsValue.get(7).substring(16));

	} catch (Exception e) {
	    return null;
	}
	return natega;
    }

    public Natega(JSONObject jsonObject) {
	name = jsonObject.getString(FIELD_NAME);
	studentCode = jsonObject.getString(FIELD_STUDENT_CODE);
	seatNumber = jsonObject.getInt(FIELD_SEAT_NUMBER);
	grade = jsonObject.getInt(FIELD_GRADE);
	department = jsonObject.getString(FIELD_DEPARTMENT);
	spec = jsonObject.getString(FIELD_SPEC);
	ta2deer = jsonObject.getString(FIELD_TA2DEER);
	total = jsonObject.getInt(FIELD_TOTAL);
    }

    public Natega() {
    }

    public JSONObject toJSON() {
	JSONObject jsonObject = new JSONObject();

	jsonObject.put(FIELD_NAME, name);
	jsonObject.put(FIELD_STUDENT_CODE, studentCode);
	jsonObject.put(FIELD_SEAT_NUMBER, seatNumber);
	jsonObject.put(FIELD_GRADE, grade);
	jsonObject.put(FIELD_DEPARTMENT, department);
	jsonObject.put(FIELD_SPEC, spec);
	jsonObject.put(FIELD_TA2DEER, ta2deer);
	jsonObject.put(FIELD_TOTAL, total);

	return jsonObject;
    }

    @Override
    public String toString() {
	String ans = "";
	ans += "≈”„ «·ÿ«·» : " + name;
	ans += "\n";
	ans += "ﬂÊœ «·ÿ«·» : " + studentCode;
	ans += "\n";
	ans += "—ﬁ„ «·Ã·Ê” : " + seatNumber;
	ans += "\n";
	ans += "«·›—ﬁ… : " + grade;
	ans += "\n";
	ans += "«· Œ’’ : " + department;
	ans += "\n";
	ans += "«·‘⁄»… : " + spec;
	ans += "\n";
	ans += "«· ﬁœÌ— «·⁄«„ : " + ta2deer;
	ans += "\n";
	ans += "„Ã„Ê⁄ «·œ—Ã«  : " + total;

	return ans;
    }

    public float getPercentage() {
	float ans = total / 15f;
	int x = (int) (ans * 1000);
	return x / 1000f;
    }

    @Override
    public int compareTo(Natega o) {
	return Integer.compare(total, o.total);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getStudentCode() {
	return studentCode;
    }

    public void setStudentCode(String studentCode) {
	this.studentCode = studentCode;
    }

    public int getSeatNumber() {
	return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
	this.seatNumber = seatNumber;
    }

    public int getGrade() {
	return grade;
    }

    public void setGrade(int grade) {
	this.grade = grade;
    }

    public String getDepartment() {
	return department;
    }

    public void setDepartment(String department) {
	this.department = department;
    }

    public String getSpec() {
	return spec;
    }

    public void setSpec(String spec) {
	this.spec = spec;
    }

    public String getTa2deer() {
	return ta2deer;
    }

    public void setTa2deer(String ta2deer) {
	this.ta2deer = ta2deer;
    }

    public int getTotal() {
	return total;
    }

    public void setTotal(int total) {
	this.total = total;
    }

}
