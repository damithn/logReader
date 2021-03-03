import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Test {

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
        System.out.println("<!-----summary report-----!>");

        System.out.println(String.format("%-12s %4d s [%2d m]", "prepare:", prepareTime / 1000000000l, (prepareTime / 1000000000l) / 60));
        System.out.println(String.format("%-12s %4d s [%2d m]", "queries:", queryTime / 1000000000l, (queryTime / 1000000000l) / 60));
        System.out.println(String.format("%-12s %4d s [%2d m]", "paginate:", paginateTime / 1000000000l, (paginateTime / 1000000000l) / 60));
        System.out.println(String.format("%-12s %4d s [%2d m]", "generate:", generateTime / 1000000000l, (generateTime / 1000000000l) / 60));
        System.out.println(String.format("%-30s %4d s [%2d m]", "    TOTAL time for processing:", totProceTime / 1000000000l, (totProceTime / 1000000000l) / 60));
        System.out.println(" ");


        try {
//            Stream<String> lines = Files.lines(Paths.get(fileName)).filter(line -> line.contains("pentaho.report.processing.query")).
//                    map(line -> {return line.replace("tag[pentaho.report.processing.query]", "");});
            Stream<String> lines = Files.lines(Paths.get(fileName)).filter(line -> line.contains("pentaho.report.processing.query"))
                    .map(line -> line.replace("tag[pentaho.report.processing.query]", ""))
                    .map(line -> line.replace("message[query=", ""));
//                    .map(fun -> {
//                        return "start time[" + Test1.convertTimeToSec(Double.valueOf(fun.substring(6, finalReadLine.indexOf("]")))) + "]" +
//                                " execution time[" + Test1.convertTimeToSec(Double.valueOf(fun.substring(finalReadLine.indexOf("time[") + 5)
//                                .substring(0, fun.substring(finalReadLine.indexOf("time[") + 5).indexOf("]")))) + "]";
//                    });


            System.out.println("<!-----Filtering the query data-----!>");
            lines.forEach(System.out::println);
            lines.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
        System.out.println(" ");
        try {
            Stream<String> lines = Files.lines(Paths.get(fileName)).filter(line -> line.contains("Summary.pentaho.report.processing.layout"))
//                    .map(fun -> {
//                        return "start time[" + Test1.convertTimeToSec(Double.valueOf(fun.substring(6, finalReadLine.indexOf("]")))) + "]" +
//                                " execution time[" + Test1.convertTimeToSec(Double.valueOf(fun.substring(finalReadLine.indexOf("time[") + 5)
//                                .substring(0, fun.substring(finalReadLine.indexOf("time[") + 5).indexOf("]")))) + "]";
//                    })
                    .map(line -> line.replace("tag[Summary.pentaho.report.processing.layout.process.", ""));
            System.out.println("<!-----Filtering the Summary layout data-----!>");
            lines.forEach(System.out::println);
            lines.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
        System.out.println(" ");
        try {
            Stream<String> lines = Files.lines(Paths.get(fileName)).filter(line -> line.contains("pentaho.report.processing.layout"))
//                    .map(fun -> {
//                        return "start time[" + Test1.convertTimeToSec(Double.valueOf(fun.substring(6, finalReadLine.indexOf("]")))) + "]" +
//                                " execution time[" + Test1.convertTimeToSec(Double.valueOf(fun.substring(finalReadLine.indexOf("time[") + 5)
//                                .substring(0, fun.substring(finalReadLine.indexOf("time[") + 5).indexOf("]")))) + "]";
//                    })
                    .map(line -> line.replace("tag[Summary.pentaho.report.processing.layout.process.", ""));
            System.out.println("<!-----Filtering the layout data-----!>");
            lines.forEach(System.out::println);
            lines.close();
        } catch (IOException io) {
            io.printStackTrace();
        }

        br.close();

    }

    public static double convertTimeToSec(double test) {
        //return (int)(test*1000)%1000;

        return (double) (test % 1000000l);
    }
}