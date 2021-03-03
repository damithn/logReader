import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class LogReader {

    public static void main(String[] args) throws Exception {
        long queryTime = 0l;
        long paginateTime = 0l;
        long prepareTime = 0l;
        long generateTime = 0l;
        long totProceTime = 0l;

        //log for single run of a report
        String fileName = "C:\\Users\\damith_samarakoon\\Downloads\\23feb\\prd-perfStats.log";
        File logFile = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(logFile));
        String readLine = br.readLine();


        String finalReadLine = readLine;
        System.out.println("<!-----Filtering the query data-----!>");

        Files.lines(Paths.get(fileName))
                .filter(line -> line.contains("pentaho.report.processing.query"))
                .map(fun -> {
                    return "start time[" + LogReader.convertTimeToDate(Double.valueOf(fun.substring(6, finalReadLine.indexOf("]")))) + "]" +
                            " execution time[" + LogReader.convertTimeToSec(Double.valueOf(fun.substring(finalReadLine.indexOf("time[") + 5)
                            .substring(0, fun.substring(finalReadLine.indexOf("time[") + 5).indexOf("]")))) + "]" +
                            fun.substring(fun.indexOf(" count[") + 0).replace("message", "");
                })
                .sorted((o1, o2) -> {
                    if (o1.substring(o1.indexOf("[query=") + 8).compareTo(o2.substring(o2.indexOf("[query=") + 8)) == 0) {
                        return o1.substring(o1.indexOf("execution time[") + 15).compareTo(o2.substring(o2.indexOf("execution time[") + 15));
                    }
                    return o1.substring(o1.indexOf("[query=") + 8).compareTo(o2.substring(o2.indexOf("[query=") + 8));
                })
                .forEach(System.out::println);

        System.out.println("");
        System.out.println("<!-----Filtering the Summary layout data-----!>");
        Files.lines(Paths.get(fileName))
                .filter(line -> line.contains("Summary.pentaho.report.processing.layout"))
                .map(fun -> {
                    return "start time[" + LogReader.convertTimeToSec(Double.valueOf(fun.substring(6, finalReadLine.indexOf("]")))) + "]" +
                            " execution time[" + LogReader.convertTimeToSec(Double.valueOf(fun.substring(finalReadLine.indexOf("time[") + 5)
                            .substring(0, fun.substring(finalReadLine.indexOf("time[") + 5).indexOf("]")))) + "]" + "[" +
                            fun.substring(fun.indexOf("process.") + 8);
                })
                .sorted((o1, o2) -> {
                    if (o1.substring(o1.indexOf("][") + 2).compareTo(o2.substring(o2.indexOf("][") + 2)) == 0) {
                        return o1.substring(o1.indexOf("execution time[") + 15).compareTo(o2.substring(o2.indexOf("execution time[") + 15));
                    }
                    return o1.substring(o1.indexOf("][") + 2).compareTo(o2.substring(o2.indexOf("][") + 2));
                })

                .forEach(System.out::println);
        System.out.println(" ");

        System.out.println("<!-----Filtering the layout data-----!>");
        Files.lines(Paths.get(fileName))
                .filter(line -> line.contains("[pentaho.report.processing.layout"))
                .map(fun -> {
                    return "start time[" + LogReader.convertTimeToSec(Double.valueOf(fun.substring(6, finalReadLine.indexOf("]")))) + "]" +
                            " execution time[" + LogReader.convertTimeToSec(Double.valueOf(fun.substring(finalReadLine.indexOf("time[") + 5)
                            .substring(0, fun.substring(finalReadLine.indexOf("time[") + 5).indexOf("]")))) + "]" + "[" +
                            fun.substring(fun.indexOf("layout.") + 6);
                })
                .sorted((o1, o2) -> {
                    if (o1.substring(o1.indexOf("][.") + 3).compareTo(o2.substring(o2.indexOf("][.") + 3)) == 0) {
                        return o1.substring(o1.indexOf("execution time[") + 15).compareTo(o2.substring(o2.indexOf("execution time[") + 15));
                    }
                    return o1.substring(o1.indexOf("][.") + 3).compareTo(o2.substring(o2.indexOf("][.") + 3));
                })
                .forEach(System.out::println);

        while (readLine != null) {
            String substring = readLine.substring(readLine.indexOf("time[") + 5);
            String numpart = substring.substring(0, substring.indexOf("]"));

            if (readLine.contains("[pentaho.report.processing.query]")) {
                queryTime = queryTime + Long.parseLong(numpart);

            }
            if (readLine.contains("[pentaho.report.processing.prepare.paginate]")) {
                paginateTime = paginateTime + Long.parseLong(numpart);

            } else if (readLine.contains("[pentaho.report.processing.prepare]")) {
                prepareTime = prepareTime + Long.parseLong(numpart);

            } else if (readLine.contains("[pentaho.report.processing.generate]")) {
                generateTime = generateTime + Long.parseLong(numpart);

            }
            if (readLine.contains("[pentaho.report.processing]")) {
                totProceTime = totProceTime + Long.parseLong(numpart);
            }
            readLine = br.readLine();

        }
        System.out.println("");
        System.out.println("");
        System.out.println("<!-----summary report-----!>");

        System.out.println(String.format("%-12s %4d s [%2d m]", "prepare:", prepareTime / 1000000000l, (prepareTime / 1000000000l) / 60));
        System.out.println(String.format("%-12s %4d s [%2d m]", "queries:", queryTime / 1000000000l, (queryTime / 1000000000l) / 60));
        System.out.println(String.format("%-12s %4d s [%2d m]", "paginate:", paginateTime / 1000000000l, (paginateTime / 1000000000l) / 60));
        System.out.println(String.format("%-12s %4d s [%2d m]", "generate:", generateTime / 1000000000l, (generateTime / 1000000000l) / 60));
        System.out.println(String.format("%-30s %4d s [%2d m]", "TOTAL time for processing:", totProceTime / 1000000000l, (totProceTime / 1000000000l) / 60));
        System.out.println(" ");
        br.close();

    }

    public static double convertTimeToSec(double test) {
        //return (int)(test*1000)%1000;
       // return (double) (test % 1000000l);
        long millis = TimeUnit.MILLISECONDS.convert((long) test, TimeUnit.NANOSECONDS);
        return millis;
    }

    public static Date convertTimeToDate(Double timeInNs) {
        String target = "1904/01/01 12:00 AM";  // Your given date string
        long nanoseconds = 13551723208300l;   // nanoseconds since target time that you want to convert to java.util.Date

        long millis = TimeUnit.MILLISECONDS.convert(nanoseconds, TimeUnit.NANOSECONDS);

        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm aaa");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = formatter.parse(target);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long newTimeInmillis = date.getTime() + millis;

        Date date2 = new Date(newTimeInmillis);
        return date2;
    }


}