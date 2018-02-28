//package com.xhs;
//
//import java.io.File;
//import java.io.IOException;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.ResponseHandler;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.CharsetUtils;
//import org.apache.http.util.EntityUtils;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//
//public class UploadPic {
//
//	public static void main(String[] args) {
//		
//		uploadVcr();
//	}
//	
//	
//	public static void uploadQuestionPic(){
//		String url = "http://127.0.0.1:8083/xhsschool/upload/qiniuUpload";
//		String root = "/Users/chenqunhui/Downloads/测试题配图/";
//		String str ="";
//		File file = new File(root);
//		if(file.exists() && file.isDirectory()){
//			String[] files = file.list();
//			for(String path :files){
//				File pathDic = new File(root+path);
//				if(pathDic.exists() && pathDic.isDirectory()){
//					String[] pics = pathDic.list();
//					for(String pic :pics){
//						CloseableHttpClient httpClient = HttpClients.createDefault();
//						
//						CloseableHttpResponse httpResponse = null;
//				        try {
//				            // 把文件转换成流对象FileBody
//				            File localFile = new File(root+path+"/"+pic);
//				            FileBody fileBody = new FileBody(localFile);
//				            // 以浏览器兼容模式运行，防止文件名乱码。
//				            HttpEntity reqEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE).addPart("file", fileBody)
//				            		.setCharset(CharsetUtils.get("UTF-8")).build();
//				            // uploadFile对应服务端类的同名属性<File类型>
//				            // .addPart("uploadFileName", uploadFileName)
//				            // uploadFileName对应服务端类的同名属性<String类型>
//				            HttpPost httpPost = new HttpPost(url);
//				            httpPost.setHeader("X-Mork-Id","1233456");
//				            httpPost.setEntity(reqEntity);
//				            
//				            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
//				                @Override
//				                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
//				                    int status = response.getStatusLine().getStatusCode();
//				                    if (status >= 200 && status < 300) {
//				                        HttpEntity entity = response.getEntity();
//				                        return entity != null ? decodeUnicode(EntityUtils.toString(entity, "utf-8")) : null;
//				                    } else {
//				                        throw new ClientProtocolException("url: "+ url +" response status:" + status +" ! ");
//				                    }
//				                }
//				            };
//				            String responseBody = httpClient.execute(httpPost, responseHandler);
//				            JSONObject obj = JSON.parseObject(responseBody).getJSONObject("data");
//				            String rurl = obj.getString("url");
//				            String name[] = obj.getString("originFileName").split("\\.")[0].split("-");
//				            String code = name[0];
//				            if(name.length ==2){
//				            	String option = name[1];
//						        System.out.println("update question_option set option_type ='pic',content='"+rurl+"' where question_code='"+code+"' and `option`='"+option+"';");
//				            }else{
//				            	str +="update question set url ='"+rurl+"' where question_code='"+code+"';/r/n";
//				            }
//				        } catch (ClientProtocolException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} finally {
//				            if (httpResponse != null) {
//				                try {
//				                    httpResponse.close();
//				                } catch (Exception e) {
//				                }
//				            }
//				        }
//					}
//				}
//			}
//		}
//		 System.out.println(str);
//		
//	}
//	
//	public static void uploadVcr(){
//		String url = "http://127.0.0.1:8083/xhsschool/upload/qiniuUpload";
//		String root = "/Users/chenqunhui/Downloads/uploadvcr/";
//		File file = new File(root);
//		CloseableHttpClient httpClient = HttpClients.createDefault();
//		if(file.exists() && file.isDirectory()){
//			String[] files = file.list();
//			for(String path :files){
//				if(!path.endsWith(".mp4")){
//					continue;
//				}
//				CloseableHttpResponse httpResponse = null;
//		        try {
//		        	File localFile = new File(root+path);
//		        	System.out.println("开始上传"+path);
//		            FileBody fileBody = new FileBody(localFile);
//		            // 以浏览器兼容模式运行，防止文件名乱码。
//		            HttpEntity reqEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE).addPart("file", fileBody)
//		            		.setCharset(CharsetUtils.get("UTF-8")).build();
//		            // uploadFile对应服务端类的同名属性<File类型>
//		            // .addPart("uploadFileName", uploadFileName)
//		            // uploadFileName对应服务端类的同名属性<String类型>
//		            HttpPost httpPost = new HttpPost(url);
//		            httpPost.setHeader("X-Mork-Id","1233456");
//		            httpPost.setEntity(reqEntity);
//		            
//		            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
//		                @Override
//		                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
//		                    int status = response.getStatusLine().getStatusCode();
//		                    if (status >= 200 && status < 300) {
//		                        HttpEntity entity = response.getEntity();
//		                        return entity != null ? decodeUnicode(EntityUtils.toString(entity, "utf-8")) : null;
//		                    } else {
//		                        throw new ClientProtocolException("url: "+ url +" response status:" + status +" ! ");
//		                    }
//		                }
//		            };
//		            String responseBody = httpClient.execute(httpPost, responseHandler);
//		            JSONObject obj = JSON.parseObject(responseBody).getJSONObject("data");
//		            String rurl = obj.getString("url");
//		            String name = obj.getString("originFileName");
//		            System.out.println(name +"  |  " +rurl);
//		            try{
//		            	file.renameTo(new File(root+path+".已传"));
//		            }catch(Exception e){
//		            	e.printStackTrace();
//		            }
//		        } catch (ClientProtocolException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} finally {
//		            if (httpResponse != null) {
//		                try {
//		                    httpResponse.close();
//		                } catch (Exception e) {
//		                }
//		            }
//		        }
//
//			}
//		}
//	    try{
//	    	httpClient.close();
//	    }catch(Exception e){
//	    	
//	    }
//		
//	}
//	
//	
//	
//	
//	  private static String decodeUnicode(String theString) {
//	        char aChar;
//	        int len = theString.length();
//	        StringBuffer outBuffer = new StringBuffer(len);
//	        for (int x = 0; x < len; ) {
//	            aChar = theString.charAt(x++);
//	            if (aChar == '\\') {
//	                aChar = theString.charAt(x++);
//	                if (aChar == 'u') {
//	                    // Read the xxxx  
//	                    int value = 0;
//	                    for (int i = 0; i < 4; i++) {
//	                        aChar = theString.charAt(x++);
//	                        switch (aChar) {
//	                            case '0':
//	                            case '1':
//	                            case '2':
//	                            case '3':
//	                            case '4':
//	                            case '5':
//	                            case '6':
//	                            case '7':
//	                            case '8':
//	                            case '9':
//	                                value = (value << 4) + aChar - '0';
//	                                break;
//	                            case 'a':
//	                            case 'b':
//	                            case 'c':
//	                            case 'd':
//	                            case 'e':
//	                            case 'f':
//	                                value = (value << 4) + 10 + aChar - 'a';
//	                                break;
//	                            case 'A':
//	                            case 'B':
//	                            case 'C':
//	                            case 'D':
//	                            case 'E':
//	                            case 'F':
//	                                value = (value << 4) + 10 + aChar - 'A';
//	                                break;
//	                            default:
//	                                throw new IllegalArgumentException(
//	                                        "Malformed   \\uxxxx   encoding.");
//	                        }
//
//	                    }
//	                    outBuffer.append((char) value);
//	                } else {
//	                    if (aChar == 't')
//	                        aChar = '\t';
//	                    else if (aChar == 'r')
//	                        aChar = '\r';
//	                    else if (aChar == 'n')
//	                        aChar = '\n';
//	                    else if (aChar == 'f')
//	                        aChar = '\f';
//	                    outBuffer.append(aChar);
//	                }
//	            } else
//	                outBuffer.append(aChar);
//	        }
//	        return outBuffer.toString();
//	    }
//}
