package esteem.jun.common.util;

import weaver.conn.RecordSet;
import weaver.docs.category.SecCategoryComInfo;
import weaver.docs.docs.*;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.file.FileManage;
import weaver.file.FileUpload;
import weaver.file.ImageInfo;
import weaver.file.multipart.DefaultFileRenamePolicy;
import weaver.general.GCONST;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.login.Base64;
import weaver.system.SystemComInfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2023-02-16 15:26
 * @Description:
 */
public class DocAttachmentUtil {
    private static ImageFileIdUpdate imageFileIdUpdate = new ImageFileIdUpdate();

    public DocAttachmentUtil() {
    }

    public User getCreateUser(int userid) {
        RecordSet rs = new RecordSet();
        User user = null;
        String sql = "select * from HrmResource where id=" + userid + "";
        rs.execute(sql);
        if (rs.next()) {
            user = new User();
            user.setUid(rs.getInt("id"));
            user.setLoginid("loginid");
            user.setFirstname(rs.getString("firstname"));
            user.setLastname(rs.getString("lastname"));
            user.setAliasname(rs.getString("aliasname"));
            user.setTitle(rs.getString("title"));
            user.setTitlelocation(rs.getString("titlelocation"));
            user.setSex(rs.getString("sex"));
            String languageidweaver = rs.getString("systemlanguage");
            user.setLanguage(Util.getIntValue(languageidweaver, 7));
            user.setTelephone(rs.getString("telephone"));
            user.setMobile(rs.getString("mobile"));
            user.setMobilecall(rs.getString("mobilecall"));
            user.setEmail(rs.getString("email"));
            user.setCountryid(rs.getString("countryid"));
            user.setLocationid(rs.getString("locationid"));
            user.setResourcetype(rs.getString("resourcetype"));
            user.setContractdate(rs.getString("contractdate"));
            user.setJobtitle(rs.getString("jobtitle"));
            user.setJobgroup(rs.getString("jobgroup"));
            user.setJobactivity(rs.getString("jobactivity"));
            user.setJoblevel(rs.getString("joblevel"));
            user.setSeclevel(rs.getString("seclevel"));
            user.setUserDepartment(Util.getIntValue(rs.getString("departmentid"), 0));
            user.setUserSubCompany1(Util.getIntValue(rs.getString("subcompanyid1"), 0));
            user.setUserSubCompany2(Util.getIntValue(rs.getString("subcompanyid2"), 0));
            user.setUserSubCompany3(Util.getIntValue(rs.getString("subcompanyid3"), 0));
            user.setUserSubCompany4(Util.getIntValue(rs.getString("subcompanyid4"), 0));
            user.setManagerid(rs.getString("managerid"));
            user.setAssistantid(rs.getString("assistantid"));
            user.setPurchaselimit(rs.getString("purchaselimit"));
            user.setCurrencyid(rs.getString("currencyid"));
            user.setLogintype("1");
            user.setAccount(rs.getString("account"));
        }

        rs.execute(" SELECT id,loginid,firstname,lastname,systemlanguage,seclevel FROM HrmResourceManager WHERE id=" + userid);
        if (rs.next()) {
            user = new User();
            user.setUid(userid);
            user.setLoginid(rs.getString("loginid"));
            user.setFirstname(rs.getString("firstname"));
            user.setLastname(rs.getString("lastname"));
            user.setLanguage(Util.getIntValue(rs.getString("systemlanguage"), 0));
            user.setSeclevel(rs.getString("seclevel"));
            user.setLogintype("1");
        }

        return user;
    }

    public int getCreatedDocIDs(String filename, String fileContent, int userid, String fileSec) {
        User user = this.getCreateUser(userid);
        DocAttachment da = new DocAttachment();
        da.setDocid(0);
        da.setImagefileid(0);
        da.setFilecontent(fileContent);
        da.setImagefilename(filename);
        da.setFilename(filename);
        da.setFilerealpath(GCONST.getSysFilePath() + "\\" + filename);
        da.setIszip(1);
        DocInfo doc = new DocInfo();
        doc.setId(0);
        doc.setDocSubject(filename);
        doc.setDoccontent("");
        doc.setMaincategory(0);
        doc.setSubcategory(0);
        doc.setSeccategory(Util.getIntValue(fileSec));
        doc.setAttachments(new DocAttachment[]{da});
        int docid = 0;

        try {
            docid = this.createDoc(doc, user);
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return docid;
    }

    private int createDoc(DocInfo doc, User user) throws Exception {
        String filename = "";

        try {
            SecCategoryComInfo scc = new SecCategoryComInfo();
            DocComInfo dc = new DocComInfo();
            DocManager dm = new DocManager();
            DocViewer dv = new DocViewer();
            DocImageManager imgManger = new DocImageManager();
            RecordSet rs = new RecordSet();
            int docId = dm.getNextDocId(rs);
            doc.setId(docId);
            DocAttachment[] docAttachments = doc.getAttachments();

            for(int i = 0; docAttachments != null && i < docAttachments.length; ++i) {
                DocAttachment da = docAttachments[i];
                if (da != null && (da.getFilecontent() != null && !"".equals(da.getFilecontent()) || da.getFilerealpath() != null && !"".equals(da.getFilerealpath()))) {
                    da = this.saveAttachment(da);
                    if (da == null || da.getImagefileid() <= 0) {
                        continue;
                    }

                    da.setDocid(docId);
                    int imageId = da.getImagefileid();
                    filename = da.getFilename();
                    String ext = "";
                    imgManger.resetParameter();
                    if ("".equals(doc.getDocSubject())) {
                        doc.setDocSubject(this.getFileMainName(filename));
                        imgManger.setImagefilename(filename);
                        ext = this.getFileExt(filename);
                    } else {
                        imgManger.setImagefilename(filename);
                        ext = this.getFileExt(filename);
                    }

                    if (ext.equalsIgnoreCase("doc")) {
                        imgManger.setDocfiletype("3");
                    } else if (ext.equalsIgnoreCase("xls")) {
                        imgManger.setDocfiletype("4");
                    } else if (ext.equalsIgnoreCase("ppt")) {
                        imgManger.setDocfiletype("5");
                    } else if (ext.equalsIgnoreCase("wps")) {
                        imgManger.setDocfiletype("6");
                    } else if (ext.equalsIgnoreCase("docx")) {
                        imgManger.setDocfiletype("7");
                    } else if (ext.equalsIgnoreCase("xlsx")) {
                        imgManger.setDocfiletype("8");
                    } else if (ext.equalsIgnoreCase("pptx")) {
                        imgManger.setDocfiletype("9");
                    } else {
                        imgManger.setDocfiletype("2");
                    }

                    imgManger.setDocid(docId);
                    imgManger.setImagefileid(imageId);
                    imgManger.setIsextfile("1");
                    imgManger.AddDocImageInfo();
                }

                docAttachments[i] = da;
            }

            doc.setAttachments(docAttachments);
            String date = "";
            String time = "";
            date = TimeUtil.getCurrentDateString();
            time = TimeUtil.getOnlyCurrentTimeString();
            dm.setId(docId);
            dm.setMaincategory(doc.getMaincategory());
            dm.setSubcategory(doc.getSubcategory());
            dm.setSeccategory(doc.getSeccategory());
            dm.setLanguageid(user.getLanguage());
            dm.setDoccontent(doc.getDoccontent());
            dm.setDocstatus("1");
            dm.setDocsubject(doc.getDocSubject());
            dm.setDoccreaterid(user.getUID());
            dm.setDocCreaterType(user.getLogintype());
            dm.setUsertype(user.getLogintype());
            dm.setOwnerid(user.getUID());
            dm.setOwnerType(user.getLogintype());
            dm.setDoclastmoduserid(user.getUID());
            dm.setDocLastModUserType(user.getLogintype());
            dm.setDoccreatedate(date);
            dm.setDoclastmoddate(date);
            dm.setDoccreatetime(time);
            dm.setDoclastmodtime(time);
            dm.setDoclangurage(user.getLanguage());
            dm.setKeyword(doc.getKeyword());
            dm.setIsapprover("0");
            dm.setIsreply("");
            dm.setDocdepartmentid(user.getUserDepartment());
            dm.setDocreplyable("1");
            dm.setAccessorycount(1);
            dm.setParentids("" + docId);
            dm.setOrderable("" + scc.getSecOrderable(doc.getSeccategory()));
            dm.setClientAddress(user.getLoginip());
            dm.setUserid(user.getUID());
            DocCoder docCoder = new DocCoder();
            dm.setDocCode(docCoder.getDocCoder("" + doc.getSeccategory()));
            dm.setDocEditionId(dm.getNextEditionId(rs));
            dm.setDocEdition(1);
            dm.AddDocInfo();
            dm.AddShareInfo();
            dc.addDocInfoCache("" + docId);
            dv.setDocShareByDoc("" + docId);
            return docId;
        } catch (Exception var16) {
            return -1;
        }
    }

    private DocAttachment saveAttachment(DocAttachment da) throws Exception {
        //int imageid = false;
        da = this.writeAttachment(da);
        if (da == null) {
            return null;
        } else {
            int filesize = da.getImagefilesize();
            String filerealpath = da.getFilerealpath();
            String imagefileused = "1";
            String iszip = da.getIszip() + "";
            String isencrypt = "0";
            String originalfilename = da.getFilename();
            String contenttype = da.getFiletype();
            RecordSet rs = new RecordSet();
            char separator = Util.getSeparator();
            int imageid = imageFileIdUpdate.getImageFileNewId();
            String para = "" + imageid + separator + originalfilename + separator + contenttype + separator + imagefileused + separator + filerealpath + separator + iszip + separator + isencrypt + separator + filesize;
            rs.executeProc("ImageFile_Insert", para);
            da.setImagefileid(imageid);
            InputStream source = null;

            try {
                if (contenttype.indexOf("image") != -1) {
                    File thefile = new File(filerealpath);
                    if (da.getIszip() == 1) {
                        ZipInputStream zin = new ZipInputStream(new FileInputStream(thefile));
                        if (zin.getNextEntry() != null) {
                            source = new BufferedInputStream(zin);
                        }
                    } else {
                        source = new BufferedInputStream(new FileInputStream(thefile));
                    }

                    ImageInfo ii = new ImageInfo();
                    ii.setInput(source);
                    if (!ii.check()) {
                        da.setImagefilewidth(0);
                        da.setImagefileheight(0);
                    } else {
                        da.setImagefilewidth(ii.getWidth());
                        da.setImagefileheight(ii.getHeight());
                    }
                } else {
                    da.setImagefilewidth(0);
                    da.setImagefileheight(0);
                }
            } catch (Exception var16) {
                da.setImagefilewidth(0);
                da.setImagefileheight(0);
            }

            return da;
        }
    }

    private DocAttachment writeAttachment(DocAttachment da) throws Exception {
        DefaultFileRenamePolicy defpolicy = new DefaultFileRenamePolicy();
        SystemComInfo syscominfo = new SystemComInfo();
        OutputStream fileOut = null;
        InputStream fileInput = null;

        try {
            boolean needzip = false;
            if (syscominfo.getNeedzip().equals("1")) {
                needzip = true;
            }

            da.setIszip(needzip ? 1 : 0);
            if (da.getFilecontent() != null && !"".equals(da.getFilecontent())) {
                byte[] fileContent = Base64.decode(da.getFilecontent());
                fileInput = new BufferedInputStream(new ByteArrayInputStream(fileContent));
            } else {
                File existedFile = new File(da.getFilerealpath());
                if (da.getFilename() == null || "".equals(da.getFilename())) {
                    da.setFilename(this.getFileMainName(existedFile.getName()));
                }

                fileInput = new BufferedInputStream(new FileInputStream(existedFile));
            }

            String saveDirectory = FileUpload.getCreateDir(syscominfo.getFilesystem());
            if (saveDirectory != null) {
                FileManage.createDir(saveDirectory);
            }

            String saveFileName = null;
            String preZipFileName = null;
            saveFileName = Util.getRandom();
            if (saveFileName == null) {
                return null;
            } else {
                int size;
                if (needzip) {
                    preZipFileName = saveFileName;
                    String body = null;
                    size = saveFileName.lastIndexOf(".");
                    if (size != -1) {
                        body = saveFileName.substring(0, size);
                    } else {
                        body = saveFileName;
                    }

                    saveFileName = body + ".zip";
                }

                File file = new File(new String(saveDirectory.getBytes("ISO8859_1"), "GBK"), new String(saveFileName.getBytes("ISO8859_1"), "GBK"));
                da.setFilerealpath(file.getPath());
                if (defpolicy != null) {
                    file = defpolicy.rename(file);
                    new String(file.getName().getBytes("GBK"), "ISO8859_1");
                }

                if (needzip) {
                    ZipOutputStream filezipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                    filezipOut.setMethod(8);
                    filezipOut.putNextEntry(new ZipEntry(new String(preZipFileName.getBytes("GBK"), "ISO8859_1")));
                    fileOut = filezipOut;
                } else {
                    fileOut = new BufferedOutputStream(new FileOutputStream(file));
                }

                size = 0;

                int read;
                for(byte[] buf = new byte[8192]; (read = fileInput.read(buf)) != -1; size += read) {
                    ((OutputStream)fileOut).write(buf, 0, read);
                }

                ((OutputStream)fileOut).flush();
                da.setImagefilesize(size);
                DocAttachment var14 = da;
                return var14;
            }
        } catch (IOException var18) {
            return null;
        } finally {
            if (fileOut != null) {
                ((OutputStream)fileOut).close();
            }

            if (fileInput != null) {
                fileInput.close();
            }

        }
    }

    private String getFileMainName(String fileName) {
        if (fileName == null) {
            return "";
        } else {
            int pos = fileName.lastIndexOf(".");
            if (pos != -1) {
                fileName = fileName.substring(0, pos);
            }

            return fileName;
        }
    }

    private String getFileExt(String file) {
        if (file != null && !file.trim().equals("")) {
            int idx = file.lastIndexOf(".");
            if (idx == -1) {
                return "";
            } else {
                return idx + 1 >= file.length() ? "" : file.substring(idx + 1);
            }
        } else {
            return "";
        }
    }

    public byte[] downLoadFromUrl(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(3000);
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        InputStream inputStream = conn.getInputStream();
        byte[] getData = this.readInputStream(inputStream);
        return getData;
    }

    public byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        //int len = false;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int len;
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }

        bos.close();
        return bos.toByteArray();
    }
}
