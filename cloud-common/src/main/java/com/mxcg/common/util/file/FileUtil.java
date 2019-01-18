package com.mxcg.common.util.file;

import com.mxcg.common.util.StringUtil;
import com.mxcg.common.util.Util;
import com.mxcg.common.util.security.MD5;
import com.mxcg.core.exception.TofocusException;
import com.mxcg.core.log.SimpleLog;


import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 
 * 文件工具类
 *
 */
public class FileUtil
{
    //private static final String SILK_CODEC_PATH = PipConfig.getString("CODEC_INS_PATH") + "/SilkDecoder.exe";
    
    /**
     * 删除文件夹 
     *
     * @param directory  文件夹
     * @throws IOException 
     **/
    
    public static void deleteDirectory(File directory)
        throws IOException
    {
        if (!directory.exists())
        {
            return;
        }
        
        cleanDirectory(directory);
        
        if (!directory.delete())
        {
            String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }
    
    /**
     * 删除文件夹里的所有内容
     *
     * @param directory directory to clean
     * @throws IOException in case cleaning is unsuccessful
     */
    public static void cleanDirectory(File directory)
        throws IOException
    {
        if (!directory.exists())
        {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }
        
        if (!directory.isDirectory())
        {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }
        
        File[] files = directory.listFiles();
        if (files == null)
        { // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }
        
        IOException exception = null;
        for (File file : files)
        {
            try
            {
                forceDelete(file);
            }
            catch (IOException ioe)
            {
                exception = ioe;
            }
        }
        
        if (null != exception)
        {
            throw exception;
        }
    }
    
    //-----------------------------------------------------------------------
    /**
     * 强力删除文件,如果是文件夹，则删除所有的文件和子文件夹
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted.
     *      (java.io.File methods returns a boolean)</li>
     * </ul>
     *
     * @param file  file or directory to delete, must not be {@code null}
     * @throws NullPointerException if the directory is {@code null}
     * @throws FileNotFoundException if the file was not found
     * @throws IOException in case deletion is unsuccessful
     */
    public static void forceDelete(File file)
        throws IOException
    {
        if (file.isDirectory())
        {
            deleteDirectory(file);
        }
        else
        {
            boolean filePresent = file.exists();
            if (!file.delete())
            {
                if (!filePresent)
                {
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }
    
    /**
     * 列出子目录名
     */
    public static List<String> listDirs(String dir)
    {
        List<String> filesList = new ArrayList<String>();
        if (dir == null)
        {
            return filesList;
        }
        
        File file = new File(dir);
        if (file.exists() && file.isDirectory())
        {
            File[] files = file.listFiles();
            for (File f : files)
            {
                
                if (f.exists() && f.isDirectory())
                {
                    String fileName = f.getName();
                    filesList.add(fileName);
                }
            }
        }
        
        return filesList;
    }

    /**
     * 读取文件夹dir下的所有文件并且保存到list数值中，如果extNames为空的话，则读取所有的子文件,
     * 如果不为空的话,则只读取文件类型在extNames中的文件。
     * 如果lastTime非空，则只返回更新时间在这个时间之前的文件
     * @param dir
     * @param extNames:文件后缀
     * @return
     */
    public static List<String> listFiles(String dir, List<String> extNames, Date lastUpdateTime, List<String> prename)
    {
        return listFiles(dir, extNames, lastUpdateTime, prename, -1);
    }
    
    /**
     * 读取文件夹dir下的所有文件并且保存到list数值中，如果extNames为空的话，则读取所有的子文件,
     * 如果不为空的话,则只读取文件类型在extNames中的文件。
     * 如果lastTime非空，则只返回更新时间在这个时间之前的文件
     * @param dir
     * @param extNames:文件后缀
     * @return
     */
    public static List<String> listFiles(String dir, List<String> extNames, Date lastUpdateTime, List<String> prename, int max)
    {
        List<String> filesList = new ArrayList<String>();
        if (dir == null)
        {
            return filesList;
        }
        
        File file = new File(dir);
        if (file.exists() && file.isDirectory())
        {
            File[] files = file.listFiles();
            for (File f : files)
            {
                
                if (f.exists() && f.isFile())
                {
                    String fileName = f.getName();
                    String extName = Util.getExtfromFilename(fileName);
                    if (extName != null)
                        extName = extName.toLowerCase();
                    String name = Util.getNamefromFilename(fileName);
                    boolean match = true;
                    if (extNames != null && extNames.size() > 0)
                    {
                        if (!extNames.contains(extName))
                            match = false;
                    }
                    if (prename != null && prename.size() > 0)
                    {
                        boolean ok = false;
                        for(String pre : prename)
                        {
                            if(name.startsWith(pre))
                            {
                                ok = true;
                                break;
                            }
                        }
                        if (!ok)
                            match = false;
                    }
                    if (lastUpdateTime != null)
                    {
                        if (f.lastModified() >= lastUpdateTime.getTime())
                            match = false;
                    }
                    if (match)
                        filesList.add(fileName);
                }
                if(max > 0 && filesList.size() >=max)
                {
                    SimpleLog.outWaring("总文件数量有" + files.length + "个，取" + max + "个文件");
                    break;
                }
            }
        }
        else
        {
            return filesList;
        }
        
        return filesList;
    }
    
    /**
     * 从文件夹中找出给定文件名的文件 
     * @param dir
     * @param fileName
     * @return
     */
    public static List<File> findFile(File dir, String fileName)
    {
        if (null == dir || !dir.exists() || !dir.isDirectory())
        {
            throw new TofocusException("目录为空,目录为: " + dir);
        }
        List<File> listFiles = new ArrayList<File>();
        listFile(listFiles, dir, fileName);
        return listFiles;
    }
    
    /**
     * 根据机修改时间和后缀文件名查找文件
     * @param dir
     * @param extNames
     * @param begintime:
     * @param endtime
     * @return
     */
    public static List<String> findFile(String dir, List<String> extNames, long begintime, long endtime)
    {
        List<String> filesList = new ArrayList<String>();
        if (dir == null)
        {
            return filesList;
        }
        
        File file = new File(dir);
        
        if (file.exists() && file.isDirectory())
        {
            File[] files = file.listFiles();
            for (File f : files)
            {
                
                if (f.exists() && f.isFile() && f.lastModified() >= begintime && f.lastModified() <= endtime)
                {
                    String fileName = f.getName();
                    String extName = Util.getExtfromFilename(fileName);
                    if (extNames != null && extNames.size() > 0)
                    {
                        for (String ext : extNames)
                        {
                            if (ext != null && extName.toLowerCase().equals(ext.toLowerCase()))
                            {
                                filesList.add(fileName);
                            }
                        }
                    }
                    else
                    {
                        filesList.add(fileName);
                    }
                    
                }
            }
        }
        else
        {
            return filesList;
        }
        
        return filesList;
    }
    
    /**
     * 采用递归遍历文件 ，如果遍历到的文件是文件，并且名字以fileName结尾的话，则 把当前文件加入到 files列表中
     * @param files
     * @param dir
     * @param fileName
     */
    public static void listFile(List<File> files, File dir, String fileName)
    {
        if (dir == null || !dir.exists())
            return;
        if (dir.isDirectory())
        {
            File listFiles[] = dir.listFiles();
            for (File file : listFiles)
            {
                listFile(files, file, fileName);
            }
        }
        else if (dir.getName().endsWith(fileName))
        {
            files.add(dir);
        }
    }
    
    static class MyFileFilter implements FilenameFilter
    {
        @Override
        public boolean accept(File dir, String name)
        {
            if (dir.getName().endsWith(name))
            {
                return true;
            }
            else
                return false;
        }
        
    }
    
    public static boolean isFileExsit(String path)
    {
        File file = new File(path);
        return file.exists();
    }
    
    public static void saveFile(String filepath, byte[] content)
    {
        if (content == null || filepath == null)
        {
            SimpleLog.outErr("content为空或filepath为空.");
            return;
        }
        RandomAccessFile file = null;
        try
        {
            if (!isFileExsit(filepath))
            {
                file = new RandomAccessFile(filepath, "rw");
                file.setLength(content.length);
                file.close();
            }
            file = new RandomAccessFile(filepath, "rw");
            file.setLength(content.length);
            file.write(content);
        }
        catch (Exception e)
        {
            SimpleLog.outException(e);
        }
        finally
        {
            try
            {
                file.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 清理重复文件，后面的路径里发现的重复文件将被清除
     * @param paths
     * @return
     */
    public static int clearDupFile(String... paths)
    {
        long nextshow = new Date().getTime() + 2000;
        int count = 0;
        if(paths != null && paths.length > 0)
        {
            //主目录
            Map<String, Set<Long>> md5Set = new HashMap<String, Set<Long>>();
            List<File> dupFile = new ArrayList<File>();
            for(String path : paths)
            {
                File file = new File(path);
                collectDupFile(md5Set, dupFile, file, false);
            }
            System.out.println("共发现 " + dupFile.size() + "个重复文件");
            for (File f : dupFile)
            {
                String path = f.getPath();
                path = path.substring(0, path.indexOf("."))+".md5";
                File md5 = new File(path);
                if(md5.exists())
                    md5.delete();
                f.delete();
                count++;
                if(nextshow > new Date().getTime())
                {
                    System.out.println("删除 " + count + "个重复文件");
                    nextshow = new Date().getTime() + 2000;
                }
            }
        }
        System.out.println("共删除 " + count + "个重复文件");
        return count;
    }
    
    private static boolean collectDupFile(Map<String, Set<Long>> md5Set, List<File> dupFile, File file, boolean pointed)
    {
        //跳过md5文件
        if(file.getName().toLowerCase().endsWith("md5"))
            return pointed;
        if (file.exists())
        {
            if (file.isFile())
            {
                String md5 = MD5.getFileMD5(file.getPath());
                long size = file.length();
                if (md5Set.containsKey(md5))
                {
                    Set<Long> s = md5Set.get(md5);
                    if (s.contains(size))
                    {
                        dupFile.add(file);
                        System.out.println();
                        System.out.print("发现第 " + dupFile.size() + "个重复文件，"+file.getName());
                        pointed = false;
                    }
                    else
                    {
                        s.add(size);
                        if(!pointed)
                            System.out.println();
                        System.out.print(".");
                        pointed = true;
                    }
                }
                else
                {
                    Set<Long> value = new HashSet<Long>();
                    value.add(size);
                    md5Set.put(md5, value);
                    if(!pointed)
                        System.out.println();
                    System.out.print(".");
                    pointed = true;
                }
            }
            else if (file.isDirectory())
            {
                System.out.println();
                System.out.print("开始检查 " + file.getName() + " 目录");
                pointed = false;
                File[] list = file.listFiles();
                for (File f : list)
                {
                    pointed = collectDupFile(md5Set, dupFile, f, pointed);
                }
            }
        }
        return pointed;
    }

    /**
     * 读取文件，segementsize 大于 1 时分段读取，小于等于 1 时读取全部内容，最大读取2G
     * @param filepath
     * @param segementnumber
     * @param segementsize
     * @return
     */
    public static byte[] readFile(String filepath, int segementnumber, int segementsize)
    {
        byte[] result = null;
        RandomAccessFile file = null;
        try
        {
            if (!FileUtil.isFileExsit(filepath))
            {
                return null;
            }
            file = new RandomAccessFile(filepath, "r");
            if (segementsize > 0)
            {
                long skip = segementsize * (segementnumber - 1);
                file.seek(skip);
                result = new byte[segementsize];
                int rlenth = file.read(result);
                if(rlenth < 0)
                    return null;
                else
                {
                if(rlenth < segementsize)
                {
                    byte[] buff = new byte[rlenth];
                    System.arraycopy(result, 0, buff, 0, rlenth);
                    result = buff;
                }
                }
            }
            else
            {
                result = new byte[(int)file.length()];
                file.read(result);
            }
        }
        catch (IOException e)
        {
            SimpleLog.outException(e);
            return null;
        }
        finally
        {
            try
            {
                file.close();
            }
            catch (IOException e)
            {
                SimpleLog.outException(e);
            }
        }
        return result;
    }
    
    /**
     * 写文件；segementsize 大于 1 时为分段写入
     * @param filepath
     * @param segement
     * @param segementnumber
     * @param segementsize
     * @return
     */
    public static boolean writeFile(String filepath, byte[] segement, int segementnumber, int segementsize)
    {
        RandomAccessFile file = null;
        try
        {
            file = new RandomAccessFile(filepath, "rw");
            if (segementsize > 0)
            {
                long skip = segementsize * (segementnumber - 1);
                file.seek(skip);
            }
            file.write(segement);
        }
        catch (IOException e)
        {
            SimpleLog.outException(e);
            return false;
        }
        finally
        {
            try
            {
                file.close();
            }
            catch (IOException e)
            {
                SimpleLog.outException(e);
            }
        }
        return true;
    }
    
    public static byte[] readFileContent(String fileName)
    {
        RandomAccessFile file = null;
        byte[] f = null;
        try
        {
            file = new RandomAccessFile(fileName, "r");
            f = new byte[(int)file.length()];
            file.read(f);
        }
        catch (Exception e)
        {
            SimpleLog.outException(e);
        }
        finally
        {
            try
            {
                if (file != null)
                    file.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        return f;
    }
    
    /**
     * 返回一个文件对象，如果不存在的话，则创建一个新的文件
     * @param fileName
     * @return
     */
    public static File getFile(String fileName)
    {
        if (fileName == null || fileName.equals(""))
        {
            return null;
        }
        File file = new File(fileName);
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (Exception e)
            {
                SimpleLog.outException(e);
                return null;
            }
        }
        return file;
    }
    
    /**
     * 生成一个CSV文件
     * @param file
     * @param source
     */
    public static void exportCSV(File file, List<String> source)
    {
        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try
        {
            if (!file.exists())
            {
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out, "GB2312");
            bw = new BufferedWriter(osw);
            for (String s : source)
            {
                bw.append(s).append("\r\n");
            }
            
        }
        catch (Exception e)
        {
            SimpleLog.outException(e);
        }
        finally
        {
            if (bw != null)
            {
                try
                {
                    bw.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (osw != null)
            {
                try
                {
                    osw.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    //移动文件
    public static synchronized boolean moveFile(String sourcePath, String destPath)
    {
        boolean result = false;
        try
        {
            if (StringUtil.isBlank(sourcePath))
                return true;
            File sourceFile = new File(sourcePath);
            if (!sourceFile.exists())
                return true;
            File dest = new File(destPath);
            if ((dest.exists()))
                dest.delete();
            result = sourceFile.renameTo(dest);
        }
        catch (Exception e)
        {
            SimpleLog.outException(e);
        }
        return result;
    }
    
    /**
     * ios版微信silk文件解码
     * @param ffmpegPath ffmpeg
     * @param silkpath silk文件路径
     * @param finalpath 最终输出的文件
     * @param type 仅能是silk
     * @param SILK_CODEC_PATH  silk编码器路径
     */
    public static boolean mediaSilkCodec(String ffmpegPath, String silkpath, String finalpath, String type, String SILK_CODEC_PATH)
    {
        if (StringUtil.isEmpty(type) || !"silk".equalsIgnoreCase(type))
        {
            SimpleLog.outErr("传入的待转码的文件类型type不正确, 仅支持silk, type=" + type);
            return false;
        }
        
        String pcmpath = finalpath.substring(0, finalpath.lastIndexOf(".")) + ".pcm";
        
        //1. 先将silk文件转成pcm
        StringBuilder cmd = new StringBuilder();
        cmd.append(" cmd /c " + SILK_CODEC_PATH + " ");
        cmd.append(silkpath + " " + pcmpath);
        
        if (exeCommond(cmd.toString()))
        {
            // 2. 将pcm转成wav
            String wavpath = pcmpath.substring(0, pcmpath.lastIndexOf(".")) + ".wav";
            if (Pcm2WavTool.convertAudioFiles(pcmpath, wavpath))
            {
                //3. 将wav转成mp3,暂时转成mp4播放
                if (mediaCodec(ffmpegPath, wavpath, finalpath, "video"))
                    return true;
                else
                    return false;
            }
            else
                return false;
        }
        else
            return false;
    }
    
    private static boolean exeCommond(String cmd)
    {
        boolean result = true;
        Process child = null;
        BufferedReader br = null;
        try
        {
            child = Runtime.getRuntime().exec(cmd);
            //防止waitFor()方法阻塞无法返回
            br = new BufferedReader(new InputStreamReader(child.getErrorStream()));
            String read;
            while ((read = br.readLine()) != null)
            {
                //System.out.println(read);
            }
            child.waitFor();
        }
        catch (Exception e)
        {
            SimpleLog.outException("音视频转码错误", e);
            result = false;
        }
        finally
        {
            child.destroy();
            try
            {
                br.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * 音频视频转码
     * <h1>注意:借助第三方ffmpeg,安装mingw并选取C Compiler，C++ Compiler，MSYS Basic System</h1>
     * @param ffmpegPath  ffmpeg路径
     * @param mediaPath   输入文件路径  
     * @param codcFilePath 输出文件路径
     * @param type audio/video文件类型
     */
    
    public static boolean mediaCodec(String ffmpegPath, String mediaPath, String codcFilePath, String type)
    {
        if (StringUtil.isEmpty(type) || !"audio,video".contains(type))
        {
            SimpleLog.outErr("传入的待转码的文件类型type不正确, 仅支持audio或video, type = " + type);
            return false;
        }
        StringBuilder cmd = new StringBuilder();
        cmd.append(" cmd /c ");
        cmd.append(ffmpegPath);
        
        if ("video".equals(type))
        {
            cmd.append(" -threads ");//工作线程数
            cmd.append(" 2 ");
            cmd.append(" -i "); // 添加参数＂-i＂，该参数指定要转换的文件
            cmd.append(mediaPath); // 添加要转换格式的视频文件的路径
            cmd.append(" -ab "); //设置音频码率（音频数据流量，一般选择32、64、96、128）
            cmd.append(" 64 ");
            cmd.append(" -ar "); //设置声音的采样频率
            cmd.append(" 22050 ");
            cmd.append(" -vcodec ");//使用libx264编码压缩视频
            cmd.append(" libx264 ");
            cmd.append(" -qscale "); //指定转换的质量, 4的质量比6高
            cmd.append(" 6 ");
            cmd.append(" -r "); //设置帧频
            cmd.append(" 24 ");
            cmd.append(" -ac "); //设置声道数
            cmd.append(" 2 ");
        }
        else if ("audio".equals(type))
        {
            cmd.append(" -i ");
            cmd.append(mediaPath);
            cmd.append(" -acodec ");//编码
            cmd.append(" libmp3lame ");//libmp3lame 或 mp3
        }
        
        cmd.append(" -y "); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
        cmd.append(codcFilePath);
        
        boolean result = exeCommond(cmd.toString());
        return result;
    }
    
    /**
     * 截取视频文件的第一帧
     * @param ffmpegPath
     * @param mediaPath
     * @param codcFilePath
     * @param lengheight
     */
    public static boolean mediaFirstFrame(String ffmpegPath, String mediaPath, String codcFilePath, String lengheight)
    {
        StringBuilder cmd = new StringBuilder();
        cmd.append(" cmd /c ");
        cmd.append(ffmpegPath);
        
        cmd.append(" -threads ");//工作线程数
        cmd.append(" 2 ");
        cmd.append(" -i "); // 添加参数＂-i＂，该参数指定要转换的文件
        cmd.append(mediaPath); // 添加要转换格式的视频文件的路径
        cmd.append(" -f "); //设置音频码率（音频数据流量，一般选择32、64、96、128）
        cmd.append(" image2 ");
        cmd.append(" -t "); //设置声音的采样频率
        cmd.append(" 0.001 ");
        cmd.append(" -s ");//使用libx264编码压缩视频
        cmd.append(lengheight);//" 352x240 "
        
        cmd.append(" -y "); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
        cmd.append(codcFilePath);
        
        boolean result = exeCommond(cmd.toString());
        return result;
    }
    
    /**
     * 音/视频格式转换
     */
    @Deprecated
    public static void mediaFormat(String ffmpegPath, String sourcePath, String targetPath, String type)
    {
        Runtime run = null;
        try
        {
            String args = "";
            if ("audio".equals(type))
            {
                args = "-acodec libmp3lame";
            }
            else if ("video".equals(type))
            {
                //args = "-ab 128 -acodec libmp3lame -ac 1 -ar 22050 -r 29.97 -qscale 6 -y";
                //args = "-ab 128 -acodec libmp3lame -ac 1 -ar 22050 -r 29.97 -b 512 -y";
                //args = "-acodec copy -vcodec libx264 -b 560k -s 960x640 -f mp4";
                //args = "-acodec copy -vcodec libx264 -b 560k -bf 6 -f mp4";
                //args = "-ar 44100 -vcodec libx264";
            }
            run = Runtime.getRuntime();
            long start = System.currentTimeMillis();
            //执行ffmpeg.exe,前面是ffmpeg.exe的地址，中间是需要转换的文件地址，后面是转换后的文件地址。-i是转换方式，意思是可编码解码，mp3编码方式采用的是libmp3lame
            Process p = run.exec("\"" + ffmpegPath + "ffmpeg\" -i \"" + sourcePath + "\" " + args + " \"" + targetPath + "\"");//
            
            //释放进程
            p.getOutputStream().close();
            p.getInputStream().close();
            p.getErrorStream().close();
            p.waitFor();
            long end = System.currentTimeMillis();
            System.out.println(sourcePath + " convert success, costs:" + (end - start) + "ms");
            //删除原来的文件
        }
        catch (Exception e)
        {
            SimpleLog.outException(e);
        }
        finally
        {
            //run调用lame解码器最后释放内存
            run.freeMemory();
        }
    }
    
    //创建文件并写入内容
    public static void createFileContent(File file, String fileContent)
    {
        BufferedWriter fw = null;
        try
        {
            file.createNewFile();
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));//指定编码格式，以免读取时中文字符异常
            fw.append(fileContent);
            fw.flush();//全部写入缓存中的内容
        }
        catch (Exception e)
        {
            SimpleLog.outException(e);
        }
        finally
        {
            if (fw != null)
            {
                try
                {
                    fw.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    //读取文件内容
    public static String readFileContent(File file)
    {
        StringBuffer fileContent = new StringBuffer();
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); // 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码,
            String str = null;
            while ((str = reader.readLine()) != null)
            {
                fileContent.append(str);
            }
        }
        catch (Exception e)
        {
            SimpleLog.outException(e);
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return fileContent.toString();
    }
    
    /************  递归删除文件及文件夹 start   *******************/
    public static boolean deleteFile(File file)
    {
        // 判断目录或文件是否存在  
        if (file.exists())
        {
            // 判断是否为文件  
            if (file.isFile())
            { // 为文件时调用删除文件方法  
                return deleteFile(file.getAbsolutePath());
            }
            else
            { // 为目录时调用删除目录方法  
                return deleteDirectory(file.getAbsolutePath());
            }
        }
        return true;
    }
    
    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    private static boolean deleteDirectory(String sPath)
    {
        
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator))
        {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory())
        {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            //删除子文件
            if (files[i].isFile())
            {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } //删除子目录
            else
            {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        //删除当前目录
        if (dirFile.delete())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static boolean deleteFile(String sPath)
    {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            file.delete();
            flag = true;
        }
        return flag;
    }
    
    /************  删除文件及文件夹 end   *******************/
    
    /**
     * 检查目录，自动层级向下新建目录;目录存在不会报错
     * @param path 如：abc/def/ghk, D:/abc/def
     */
    public static void checkDirectory(String path)
    {
        if (StringUtil.isEmpty(path))
            return;
        Path newdir = FileSystems.getDefault().getPath(path);
        //Path newdir = Paths.get(path);
        try
        {
            if (!Files.exists(newdir))
                Files.createDirectories(newdir);
        }
        catch (IOException e)
        {
            SimpleLog.outException(e);
        }
    }
    
    /**
     * 移动文件，目标目录需存在，目标存在则覆盖
     * @param sourcefile 如：C:/rafaelnadal/rafa_2.jpg
     * @param targetfile 如：C:/rafaelnadal/photos/rafa_2.jpg
     */
    public synchronized static void moveFile2(String sourcefile, String targetfile)
    {
        if (StringUtil.isEmpty(sourcefile) || StringUtil.isEmpty(targetfile))
            return;
        Path movefrom = FileSystems.getDefault().getPath(sourcefile);
        Path moveto = FileSystems.getDefault().getPath(targetfile);
        try
        {
            Files.move(movefrom, moveto, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            SimpleLog.outException(e);
        }
    }
    
    /**
     * 复制文件，目标目录需存在，目标存在则覆盖
     * @param sourcefile 如：C:/rafaelnadal/rafa_2.jpg
     * @param targetfile 如：C:/rafaelnadal/photos/rafa_2.jpg
     */
    public synchronized static void copyFile(String sourcefile, String targetfile)
    {
        if (StringUtil.isEmpty(sourcefile) || StringUtil.isEmpty(targetfile))
            return;
        Path copyfrom = FileSystems.getDefault().getPath(sourcefile);
        Path copyto = FileSystems.getDefault().getPath(targetfile);
        try
        {
            Files.copy(copyfrom, copyto, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            SimpleLog.outException(e);
        }
    }
    
    /**
     * 移动文件夹，目标存在则覆盖
     * @param movefrom 如：C:/rafaelnadal/
     * @param moveto 如：D:/rafaelnadal/photos
     */
    public synchronized static void moveFolder(String movefrom, String moveto)
    {
        if (StringUtil.isEmpty(movefrom) || StringUtil.isEmpty(moveto))
            return;
        
        Path from = FileSystems.getDefault().getPath(movefrom);
        Path to = FileSystems.getDefault().getPath(moveto);
        try
        {
            Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            SimpleLog.outException(e);
        }
    }
    
    public static byte[] getBytes(File file)
    {
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }
    
    /** 
     * 处理大文件
     * @author hh
     */
    public static byte[] getBytes(String filename)
        throws IOException
    {
        FileChannel fc = null;
        try
        {
            fc = new RandomAccessFile(filename, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size()).load();
            byte[] result = new byte[(int)fc.size()];
            if (byteBuffer.remaining() > 0)
            {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        }
        catch (IOException e)
        {
            SimpleLog.outException(e);
            throw e;
        }
        finally
        {
            try
            {
                fc.close();
            }
            catch (IOException e)
            {
                SimpleLog.outException(e);
            }
        }
    }
    
    /**
     * 生成文件路径 <br>
     * 若file为空，则只生成路径(末尾不带文件分隔符)；否则，生成带有file的全路径
     * @param file eg. 操作文档_wiki.doc
     * @param paths List&lt;String&gt; or new String[]{"asd", "ds", "dfs"}
     * @return 路径
     */
    public static String getPath(String file, String... paths)
    {
        if (paths == null || paths.length == 0) return null;
        StringBuilder pathtemp = new StringBuilder();
        for (String path : paths)
        {
            if (path != null && path.trim().length() > 0)
            pathtemp.append(path).append(File.separator);
        }
        checkDirectory(pathtemp.toString());
        if (file != null) return pathtemp.append(file).toString();
        else return pathtemp.substring(0, pathtemp.lastIndexOf(File.separator));
    }
    
    public static void main(String[] args)
    {
        //        checkDirectory("lok/dds/sdf/hfgh/hgfh/ghdfh/hfh/df/dff/dad/gfd/rqwr/fef/");
        //        checkDirectory("D:/lok/dds/sdf/hfgh/hgfh/ghdfh/hfh/df/dff/dad/gfd/rqwr/fef/");
        //mediaFormat("E:\\test\\", "E:\\Workspaces\\MyEclipse 10\\pip-v2.0\\pip-web\\src\\main\\webapp\\audio\\wechat\\20150729\\F32454D40D12542DDB745F01FF4EF99D", "E:\\Workspaces\\MyEclipse 10\\pip-v2.0\\pip-web\\src\\main\\webapp\\audio\\wechat\\20150729\\test.mp3");
        //mediaFormat("E:\\test\\", "E:\\Workspaces\\MyEclipse 10\\pip-v2.0\\pip-web\\src\\main\\webapp\\media\\video\\clue\\20150730\\DC6BF6F263D48C916C26180D20DD7141", "E:\\Workspaces\\MyEclipse 10\\pip-v2.0\\pip-web\\src\\main\\webapp\\media\\video\\clue\\20150730\\DC6BF6F263D48C916C26180D20DD7141.mp4", "video");
        mediaFirstFrame("C:\\MinGW\\msys\\1.0\\bin\\ffmpeg.exe",
            "C:/server/dist/media/wechat/video/20151023/BA26F0B78F33287CE9E35AC133D1C90C.mp4",
            "C:/server/dist/media/wechat/video/20151023/BA26F0B78F33287CE9E35AC133D1C90C.jpg",
            "352*240");
    }
}
