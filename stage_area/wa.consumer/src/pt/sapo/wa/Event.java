package pt.sapo.wa;

import org.caudexorigo.text.StringUtils;

public class Event
{
	private static final String clean(String in)
	{
		if ("null".equals(in))
		{
			return "";
		}
		else
		{
			return StringUtils.trimToEmpty(in);
		}
	}

	private String type;
	private long timestamp;
	private String siteKey;

	private long userGlobalVisitorId;
	private long userVisitId;
	private long userVisitorId;
	private long userVisitStart;

	private Boolean isNewGlobalVisitorId;
	private Boolean isNewVisitorId;
	private Boolean isNewVisitId;

	private boolean hasTimingInfo;
	private long serverTime;
	private long processingTime;
	private long totalTime;

	private String userSourceKeywords;
	private String userSourceReferrer;
	private String userSourceType;

	private String userVisitorType;
	private String userAgentBrowserName;
	private String userAgentBrowserVersion;
	private String userAgentCharset;
	private String userAgentFlashVersion;
	private String userAgentLanguage;
	private String userAgentOsName;
	private String userAgentOsVersion;
	private String userAgentScreenResolution;
	private String userAgentUaString;
	private boolean userAgentJavaEnabled;

	private int userAgentColorDepth;
	private String contentHost;
	private String contentKeywords;
	private String contentPath;
	private String contentReferer;
	private String contentTitle;
	private String nsChannel;
	private String nsContent;
	private String nsSection;
	private String nsSectionGroup;
	private String nsSubsection;
	private String nsSubsectionGroup;

	private String userIp;
	private String geoCountry;
	private String geoCountryId;
	private String geoDistrict;
	private String geoDistrictId;
	private String geoLatitude;
	private String geolongitude;
	private String geoMunicipality;
	private String geoMunicipalityId;
	private String geoParish;
	private String geoParishId;
	private String geoProvider;

	private String extraGoal;
	private String extraMetadata;

	private boolean isInError = false;

	private String errorMessage = null;

	public String getContentHost()
	{
		return contentHost;
	}

	public String getContentKeywords()
	{
		return contentKeywords;
	}

	public String getContentPath()
	{
		return contentPath;
	}

	public String getContentReferer()
	{
		return contentReferer;
	}

	public String getContentTitle()
	{
		return contentTitle;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public String getExtraGoal()
	{
		return extraGoal;
	}

	public String getExtraMetadata()
	{
		return extraMetadata;
	}

	public String getGeoCountry()
	{
		return geoCountry;
	}

	public String getGeoCountryId()
	{
		return geoCountryId;
	}

	public String getGeoDistrict()
	{
		return geoDistrict;
	}

	public String getGeoDistrictId()
	{
		return geoDistrictId;
	}

	public String getGeoLatitude()
	{
		return geoLatitude;
	}

	public String getGeolongitude()
	{
		return geolongitude;
	}

	public String getGeoLongitude()
	{
		return geolongitude;
	}

	public String getGeoMunicipality()
	{
		return geoMunicipality;
	}

	public String getGeoMunicipalityId()
	{
		return geoMunicipalityId;
	}

	public String getGeoParish()
	{
		return geoParish;
	}

	public String getGeoParishId()
	{
		return geoParishId;
	}

	public String getGeoProvider()
	{
		return geoProvider;
	}

	public String getNsChannel()
	{
		return nsChannel;
	}

	public String getNsContent()
	{
		return nsContent;
	}

	public String getNsSection()
	{
		return nsSection;
	}

	public String getNsSectionGroup()
	{
		return nsSectionGroup;
	}

	public String getNsSubsection()
	{
		return nsSubsection;
	}

	public String getNsSubsectionGroup()
	{
		return nsSubsectionGroup;
	}

	public String getSiteKey()
	{
		return siteKey;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public String getType()
	{
		return type;
	}

	public String getUserAgentBrowserName()
	{
		return userAgentBrowserName;
	}

	public String getUserAgentBrowserVersion()
	{
		return userAgentBrowserVersion;
	}

	public String getUserAgentCharset()
	{
		return userAgentCharset;
	}

	public int getUserAgentColorDepth()
	{
		return userAgentColorDepth;
	}

	public String getUserAgentFlashVersion()
	{
		return userAgentFlashVersion;
	}

	public String getUserAgentLanguage()
	{
		return userAgentLanguage;
	}

	public String getUserAgentOsName()
	{
		return userAgentOsName;
	}

	public String getUserAgentOsVersion()
	{
		return userAgentOsVersion;
	}

	public String getUserAgentScreenResolution()
	{
		return userAgentScreenResolution;
	}

	public String getUserAgentUaString()
	{
		return userAgentUaString;
	}

	public long getUserGlobalVisitorId()
	{
		return userGlobalVisitorId;
	}

	public String getUserIp()
	{
		return userIp;
	}

	public String getUserSourceKeywords()
	{
		return userSourceKeywords;
	}

	public String getUserSourceReferrer()
	{
		return userSourceReferrer;
	}

	public String getUserSourceType()
	{
		return userSourceType;
	}

	public long getUserVisitId()
	{
		return userVisitId;
	}

	public long getUserVisitorId()
	{
		return userVisitorId;
	}

	public String getUserVisitorType()
	{
		return userVisitorType;
	}

	public long getUserVisitStart()
	{
		return userVisitStart;
	}

	public boolean isInError()
	{
		return isInError;
	}

	public boolean isUserAgentJavaEnabled()
	{
		return userAgentJavaEnabled;
	}

	public void setContentHost(String contentHost)
	{
		this.contentHost = StringUtils.trimToEmpty(contentHost);
	}

	public void setContentKeywords(String contentKeywords)
	{
		this.contentKeywords = contentKeywords;
	}

	public void setContentPath(String contentPath)
	{
		this.contentPath = contentPath;
	}

	public void setContentReferer(String contentReferer)
	{
		this.contentReferer = contentReferer;
	}

	public void setContentTitle(String contentTitle)
	{
		this.contentTitle = contentTitle;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public void setExtraGoal(String extraGoal)
	{
		this.extraGoal = extraGoal;
	}

	public void setExtraMetadata(String extraMetadata)
	{
		this.extraMetadata = extraMetadata;
	}

	public void setGeoCountry(String geoCountry)
	{
		this.geoCountry = geoCountry;
	}

	public void setGeoCountryId(String geoCountryId)
	{
		this.geoCountryId = geoCountryId;
	}

	public void setGeoDistrict(String geoDistrict)
	{
		this.geoDistrict = geoDistrict;
	}

	public void setGeoDistrictId(String geoDistrictId)
	{
		this.geoDistrictId = geoDistrictId;
	}

	public void setGeoLatitude(String geoLatitude)
	{
		this.geoLatitude = geoLatitude;
	}

	public void setGeolongitude(String geolongitude)
	{
		this.geolongitude = geolongitude;
	}

	public void setGeoLongitude(String geolongitude)
	{
		this.geolongitude = geolongitude;
	}

	public void setGeoMunicipality(String geoMunicipality)
	{
		this.geoMunicipality = geoMunicipality;
	}

	public void setGeoMunicipalityId(String geoMunicipalityId)
	{
		this.geoMunicipalityId = geoMunicipalityId;
	}

	public void setGeoParish(String geoParish)
	{
		this.geoParish = geoParish;
	}

	public void setGeoParishId(String geoParishId)
	{
		this.geoParishId = geoParishId;
	}

	public void setGeoProvider(String geoProvider)
	{
		this.geoProvider = geoProvider;
	}

	public void setInError(boolean isInError)
	{
		this.isInError = isInError;
	}

	public void setNsChannel(String nsChannel)
	{
		this.nsChannel = nsChannel;
	}

	public void setNsContent(String nsContent)
	{
		this.nsContent = nsContent;
	}

	public void setNsSection(String nsSection)
	{
		this.nsSection = nsSection;
	}

	public void setNsSectionGroup(String nsSectionGroup)
	{
		this.nsSectionGroup = nsSectionGroup;
	}

	public void setNsSubsection(String nsSubsection)
	{
		this.nsSubsection = nsSubsection;
	}

	public void setNsSubsectionGroup(String nsSubsectionGroup)
	{
		this.nsSubsectionGroup = nsSubsectionGroup;
	}

	public void setSiteKey(String siteKey)
	{
		this.siteKey = siteKey;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setUserAgentBrowserName(String userAgentBrowserName)
	{
		this.userAgentBrowserName = userAgentBrowserName;
	}

	public void setUserAgentBrowserVersion(String userAgentBrowserVersion)
	{
		this.userAgentBrowserVersion = userAgentBrowserVersion;
	}

	public void setUserAgentCharset(String userAgentCharset)
	{
		this.userAgentCharset = userAgentCharset;
	}

	public void setUserAgentColorDepth(int userAgentColorDepth)
	{
		this.userAgentColorDepth = userAgentColorDepth;
	}

	public void setUserAgentFlashVersion(String userAgentFlashVersion)
	{
		this.userAgentFlashVersion = userAgentFlashVersion;
	}

	public void setUserAgentJavaEnabled(boolean userAgentJavaEnabled)
	{
		this.userAgentJavaEnabled = userAgentJavaEnabled;
	}

	public void setUserAgentLanguage(String userAgentLanguage)
	{
		this.userAgentLanguage = userAgentLanguage;
	}

	public void setUserAgentOsName(String userAgentOsName)
	{
		this.userAgentOsName = userAgentOsName;
	}

	public void setUserAgentOsVersion(String userAgentOsVersion)
	{
		this.userAgentOsVersion = userAgentOsVersion;
	}

	public void setUserAgentScreenResolution(String userAgentScreenResolution)
	{
		this.userAgentScreenResolution = userAgentScreenResolution;
	}

	public void setUserAgentUaString(String userAgentUaString)
	{
		this.userAgentUaString = userAgentUaString;
	}

	public void setUserGlobalVisitorId(long userGlobalVisitorId)
	{
		this.userGlobalVisitorId = userGlobalVisitorId;
	}

	public void setUserIp(String userIp)
	{
		this.userIp = userIp;
	}

	public void setUserSourceKeywords(String userSourceKeywords)
	{
		this.userSourceKeywords = userSourceKeywords;
	}

	public void setUserSourceReferrer(String userSourceReferrer)
	{
		this.userSourceReferrer = userSourceReferrer;
	}

	public void setUserSourceType(String userSourceType)
	{
		this.userSourceType = userSourceType;
	}

	public void setUserVisitId(long userVisitId)
	{
		this.userVisitId = userVisitId;
	}

	public void setUserVisitorId(long userVisitorId)
	{
		this.userVisitorId = userVisitorId;
	}

	public void setUserVisitorType(String userVisitorType)
	{
		this.userVisitorType = userVisitorType;
	}

	public void setUserVisitStart(long userVisitStart)
	{
		this.userVisitStart = userVisitStart;
	}

	public Boolean getIsNewGlobalVisitorId()
	{
		return isNewGlobalVisitorId;
	}

	public void setIsNewGlobalVisitorId(Boolean isNewGlobalVisitor)
	{
		this.isNewGlobalVisitorId = isNewGlobalVisitor;
	}

	public Boolean getIsNewVisitorId()
	{
		return isNewVisitorId;
	}

	public void setIsNewVisitorId(Boolean isNewVisitor)
	{
		this.isNewVisitorId = isNewVisitor;
	}

	public Boolean getIsNewVisitId()
	{
		return isNewVisitId;
	}

	public void setIsNewVisitId(Boolean isNewVisit)
	{
		this.isNewVisitId = isNewVisit;
	}

	public boolean hasTimingInfo()
	{
		return hasTimingInfo;
	}

	public void setHasTimingInfo(boolean hasTimingInfo)
	{
		this.hasTimingInfo = hasTimingInfo;
	}
	
	public long getServerTime()
	{
		return serverTime;
	}

	public void setServerTime(long serverTime)
	{
		this.serverTime = serverTime;
	}

	public long getProcessingTime()
	{
		return processingTime;
	}

	public void setProcessingTime(long processingTime)
	{
		this.processingTime = processingTime;
	}

	public long getTotalTime()
	{
		return totalTime;
	}

	public void setTotalTime(long totalTime)
	{
		this.totalTime = totalTime;
	}

	public String toKeyValueString()
	{
		return String
				.format("type\t%s\ttimestamp\t%s\tsiteKey\t%s\tuserGlobalVisitorId\t%s\tuserVisitId\t%s\tuserVisitorId\t%s\tuserVisitStart\t%s\tuserSourceKeywords\t%s\tuserSourceReferrer\t%s\tuserSourceType\t%s\tuserVisitorType\t%s\tuserAgentBrowserName\t%s\tuserAgentBrowserVersion\t%s\tuserAgentCharset\t%s\tuserAgentFlashVersion\t%s\tuserAgentLanguage\t%s\tuserAgentOsName\t%s\tuserAgentOsVersion\t%s\tuserAgentScreenResolution\t%s\tuserAgentUaString\t%s\tuserAgentJavaEnabled\t%s\tuserAgentColorDepth\t%s\tcontentHost\t%s\tcontentKeywords\t%s\tcontentPath\t%s\tcontentReferer\t%s\tcontentTitle\t%s\tnsChannel\t%s\tnsContent\t%s\tnsSection\t%s\tnsSectionGroup\t%s\tnsSubsection\t%s\tnsSubsectionGroup\t%s\tuserIp\t%s\tgeoCountry\t%s\tgeoCountryId\t%s\tgeoDistrict\t%s\tgeoDistrictId\t%s\tgeoLatitude\t%s\tgeolongitude\t%s\tgeoMunicipality\t%s\tgeoMunicipalityId\t%s\tgeoParish\t%s\tgeoParishId\t%s\tgeoProvider\t%s\textraGoal\t%s\textraMetadata\t%s",
						clean(type), timestamp, clean(siteKey), userGlobalVisitorId, userVisitId, userVisitorId, userVisitStart, clean(userSourceKeywords), clean(userSourceReferrer), clean(userSourceType), clean(userVisitorType), clean(userAgentBrowserName), clean(userAgentBrowserVersion), clean(userAgentCharset), clean(userAgentFlashVersion), clean(userAgentLanguage), clean(userAgentOsName), clean(userAgentOsVersion), clean(userAgentScreenResolution), clean(userAgentUaString), userAgentJavaEnabled, userAgentColorDepth, clean(contentHost), clean(contentKeywords), clean(contentPath), clean(contentReferer), clean(contentTitle), clean(nsChannel), clean(nsContent), clean(nsSection), clean(nsSectionGroup), clean(nsSubsection), clean(nsSubsectionGroup), clean(userIp), clean(geoCountry),
						clean(geoCountryId), clean(geoDistrict), clean(geoDistrictId), clean(geoLatitude), clean(geolongitude), clean(geoMunicipality), clean(geoMunicipalityId), clean(geoParish), clean(geoParishId), clean(geoProvider), clean(extraGoal), clean(extraMetadata));
	}

	@Override
	public String toString()
	{
		return String
				.format("Event [type=%s, timestamp=%s, siteKey=%s, userGlobalVisitorId=%s, userVisitId=%s, userVisitorId=%s, userVisitStart=%s, userSourceKeywords=%s, userSourceReferrer=%s, userSourceType=%s, userVisitorType=%s, userAgentBrowserName=%s, userAgentBrowserVersion=%s, userAgentCharset=%s, userAgentFlashVersion=%s, userAgentLanguage=%s, userAgentOsName=%s, userAgentOsVersion=%s, userAgentScreenResolution=%s, userAgentUaString=%s, userAgentJavaEnabled=%s, userAgentColorDepth=%s, contentHost=%s, contentKeywords=%s, contentPath=%s, contentReferer=%s, contentTitle=%s, nsChannel=%s, nsContent=%s, nsSection=%s, nsSectionGroup=%s, nsSubsection=%s, nsSubsectionGroup=%s, userIp=%s, geoCountry=%s, geoCountryId=%s, geoDistrict=%s, geoDistrictId=%s, geoLatitude=%s, geolongitude=%s, geoMunicipality=%s, geoMunicipalityId=%s, geoParish=%s, geoParishId=%s, geoProvider=%s, extraGoal=%s, extraMetadata=%s, isInError=%s, errorMessage=%s]",
						type, timestamp, siteKey, userGlobalVisitorId, userVisitId, userVisitorId, userVisitStart, userSourceKeywords, userSourceReferrer, userSourceType, userVisitorType, userAgentBrowserName, userAgentBrowserVersion, userAgentCharset, userAgentFlashVersion, userAgentLanguage, userAgentOsName, userAgentOsVersion, userAgentScreenResolution, userAgentUaString, userAgentJavaEnabled, userAgentColorDepth, contentHost, contentKeywords, contentPath, contentReferer, contentTitle, nsChannel, nsContent, nsSection, nsSectionGroup, nsSubsection, nsSubsectionGroup, userIp, geoCountry, geoCountryId, geoDistrict, geoDistrictId, geoLatitude, geolongitude, geoMunicipality, geoMunicipalityId, geoParish, geoParishId, geoProvider, extraGoal, extraMetadata, isInError, errorMessage);
	}
}