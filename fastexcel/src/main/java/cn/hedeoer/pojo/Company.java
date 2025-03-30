package cn.hedeoer.pojo;

import cn.idev.excel.annotation.ExcelProperty;

import java.time.LocalDate;
import java.util.Objects;


public class Company {

    // 指定属性映射的excel列号
    @ExcelProperty(index = 1)
    private Integer serialNo;              // 序号

    @ExcelProperty(index = 2)
    private String unifiedSocialCreditCode; // 统一社会信用代码

    @ExcelProperty(index = 3)
    private String companyName;            // 企业名称

    @ExcelProperty(index = 4)
    private String legalRepresentative;    // 法定代表人（负责人）

    @ExcelProperty(index = 5)
    private String businessStatus;         // 经营状态

    @ExcelProperty(index = 6)
    private String companyType;            // 企业类型

    @ExcelProperty(index = 7)
    private String registeredCapital;       // 注册资本（单位可能需单独字段，如 currency）

    @ExcelProperty(index = 8)
    private String administrativeDivision;  // 行政区划

    @ExcelProperty(index = 9)
    private String businessAddress;        // 经营地址

    @ExcelProperty(index = 10)
    private String businessScope;          // 经营范围

    @ExcelProperty(index = 11)
    private LocalDate establishmentDate;        // 成立时间（建议用 Date/LocalDate 类型）

    @ExcelProperty(index = 12)
    private String registrationAuthority;   // 登记机关

    @ExcelProperty(index = 13)
    private String governingUnit;          // 管辖单位

    @ExcelProperty(index = 14)
    private String industryCategoryMajor;  // 行业类型大类

    @ExcelProperty(index = 15)
    private String industryCategoryMedium; // 行业类型中类

    @ExcelProperty(index = 16)
    private String industryCategoryMinor;  // 行业类型细类

    @ExcelProperty(index = 17)
    private Integer employeeCount;         // 从业人数（个）

    public Company() {
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getUnifiedSocialCreditCode() {
        return unifiedSocialCreditCode;
    }

    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
        this.unifiedSocialCreditCode = unifiedSocialCreditCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(serialNo, company.serialNo) && Objects.equals(unifiedSocialCreditCode, company.unifiedSocialCreditCode) && Objects.equals(companyName, company.companyName) && Objects.equals(legalRepresentative, company.legalRepresentative) && Objects.equals(businessStatus, company.businessStatus) && Objects.equals(companyType, company.companyType) && Objects.equals(registeredCapital, company.registeredCapital) && Objects.equals(administrativeDivision, company.administrativeDivision) && Objects.equals(businessAddress, company.businessAddress) && Objects.equals(businessScope, company.businessScope) && Objects.equals(establishmentDate, company.establishmentDate) && Objects.equals(registrationAuthority, company.registrationAuthority) && Objects.equals(governingUnit, company.governingUnit) && Objects.equals(industryCategoryMajor, company.industryCategoryMajor) && Objects.equals(industryCategoryMedium, company.industryCategoryMedium) && Objects.equals(industryCategoryMinor, company.industryCategoryMinor) && Objects.equals(employeeCount, company.employeeCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNo, unifiedSocialCreditCode, companyName, legalRepresentative, businessStatus, companyType, registeredCapital, administrativeDivision, businessAddress, businessScope, establishmentDate, registrationAuthority, governingUnit, industryCategoryMajor, industryCategoryMedium, industryCategoryMinor, employeeCount);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getAdministrativeDivision() {
        return administrativeDivision;
    }

    public void setAdministrativeDivision(String administrativeDivision) {
        this.administrativeDivision = administrativeDivision;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public LocalDate getEstablishmentDate() {
        return establishmentDate;
    }

    public void setEstablishmentDate(LocalDate establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    public String getRegistrationAuthority() {
        return registrationAuthority;
    }

    public void setRegistrationAuthority(String registrationAuthority) {
        this.registrationAuthority = registrationAuthority;
    }

    public String getGoverningUnit() {
        return governingUnit;
    }

    public void setGoverningUnit(String governingUnit) {
        this.governingUnit = governingUnit;
    }

    public String getIndustryCategoryMajor() {
        return industryCategoryMajor;
    }

    public void setIndustryCategoryMajor(String industryCategoryMajor) {
        this.industryCategoryMajor = industryCategoryMajor;
    }

    public String getIndustryCategoryMedium() {
        return industryCategoryMedium;
    }

    public void setIndustryCategoryMedium(String industryCategoryMedium) {
        this.industryCategoryMedium = industryCategoryMedium;
    }

    public String getIndustryCategoryMinor() {
        return industryCategoryMinor;
    }

    public void setIndustryCategoryMinor(String industryCategoryMinor) {
        this.industryCategoryMinor = industryCategoryMinor;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    @Override
    public String toString() {
        return "Company{" +
                "serialNo=" + serialNo +
                ", unifiedSocialCreditCode='" + unifiedSocialCreditCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", legalRepresentative='" + legalRepresentative + '\'' +
                ", businessStatus='" + businessStatus + '\'' +
                ", companyType='" + companyType + '\'' +
                ", registeredCapital='" + registeredCapital + '\'' +
                ", administrativeDivision='" + administrativeDivision + '\'' +
                ", businessAddress='" + businessAddress + '\'' +
                ", businessScope='" + businessScope + '\'' +
                ", establishmentDate=" + establishmentDate +
                ", registrationAuthority='" + registrationAuthority + '\'' +
                ", governingUnit='" + governingUnit + '\'' +
                ", industryCategoryMajor='" + industryCategoryMajor + '\'' +
                ", industryCategoryMedium='" + industryCategoryMedium + '\'' +
                ", industryCategoryMinor='" + industryCategoryMinor + '\'' +
                ", employeeCount=" + employeeCount +
                '}';
    }

    public Company(Integer serialNo, String unifiedSocialCreditCode, String companyName, String legalRepresentative, String businessStatus, String companyType, String registeredCapital, String administrativeDivision, String businessAddress, String businessScope, LocalDate establishmentDate, String registrationAuthority, String governingUnit, String industryCategoryMajor, String industryCategoryMedium, String industryCategoryMinor, Integer employeeCount) {
        this.serialNo = serialNo;
        this.unifiedSocialCreditCode = unifiedSocialCreditCode;
        this.companyName = companyName;
        this.legalRepresentative = legalRepresentative;
        this.businessStatus = businessStatus;
        this.companyType = companyType;
        this.registeredCapital = registeredCapital;
        this.administrativeDivision = administrativeDivision;
        this.businessAddress = businessAddress;
        this.businessScope = businessScope;
        this.establishmentDate = establishmentDate;
        this.registrationAuthority = registrationAuthority;
        this.governingUnit = governingUnit;
        this.industryCategoryMajor = industryCategoryMajor;
        this.industryCategoryMedium = industryCategoryMedium;
        this.industryCategoryMinor = industryCategoryMinor;
        this.employeeCount = employeeCount;
    }
}
