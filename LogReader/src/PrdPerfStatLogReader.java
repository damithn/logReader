import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PrdPerfStatLogReader {

    public static void main(String[] args) throws Exception {
        //log for single run of a report
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\damith_samarakoon\\Downloads\\23feb\\prd-perfStats.log"));

        long queryTime = 0l;
        long paginateTime = 0l;
        long prepareTime = 0l;
        long generateTime = 0l;
        long totProceTime = 0l;
        long mainQuery = 0l;
        long headersQuery = 0l;
        long otherQuery = 0l;
        long subMainQuery = 0l;
        long startTime =0l;
        long processTime =0l;
        long processLayOutTime =0l;

        String readLine = br.readLine();

        while (readLine != null) {

            String substring = readLine.substring(readLine.indexOf("time[") + 5);
          // System.out.println("substring======================"+substring);
            String numpart = substring.substring(0, substring.indexOf("]"));
            //System.out.println("================="+ numpart);

            String substring1 = readLine.substring(readLine.indexOf("start[") + 6);
           // System.out.println("substring1======================" + substring1);
            String numpart1 = substring1.substring(0, substring1.indexOf("]"));

            String substring2 = readLine.substring(readLine.indexOf("count[") + 0);
            System.out.println("substring2======================" + substring2);
            String numpart2 = substring2.substring(0, substring1.indexOf("]"));
            System.out.println("substring2======================" + numpart2);


            if (readLine.contains("query={MainQuery}")) {
//                subMainQuery = Long.parseLong(numpart);
//                startTime = Long.parseLong(numpart1);
                mainQuery = mainQuery + Long.parseLong(numpart);
                List<Long> mainQList = new ArrayList<Long>();
                mainQList.add(mainQuery);


                //System.out.println("Start Time : "+startTime + " Execution Time : "+subMainQuery);
//                System.out.println("***************** Main Query *************");
//                System.out.println(String.format("%-20s %4d  [%2d m] %-35s %4d s [%2d m]", "StartTime:", startTime , startTime / 1000000000l / 60,"Execution Time :", subMainQuery / 1000000000l, subMainQuery / 1000000000l / 60));
//                System.out.println("");
                //System.out.println(String.format("test","test",startTime / 1000000000l,startTime/1000000000l/60));
            }
            if (readLine.contains("query={Headers_Query}")) {
                headersQuery = headersQuery + Long.parseLong(numpart);
            }

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
//            if (readLine.contains("[Summary.pentaho.report.processing.layout]")) {
//                processLayOutTime = processLayOutTime + Long.parseLong(numpart2);
//                System.out.println("==============" + processLayOutTime);
//            }
            if (readLine.contains("[pentaho.report.processing]")) {
                totProceTime = totProceTime + Long.parseLong(numpart);
            }
            readLine = br.readLine();

        }
        otherQuery = queryTime - (mainQuery + headersQuery);

        System.out.println(String.format("%-30s %4d s [%2d m]", "total time taken -> prepare:", prepareTime / 1000000000l, (prepareTime / 1000000000l) / 60));
        System.out.println(String.format("%-12s %4d s [%2d m]", "total time taken -> queries:", queryTime / 1000000000l, (queryTime / 1000000000l) / 60));
        System.out.println(String.format("%-30s %4d s [%2d m]", "    TOTAL time for Main Query:", mainQuery / 1000000000l, mainQuery / 1000000000l / 60));
       // System.out.println(String.format("%-46s %4d s [%2d m]", "    TOTAL time for Main Query:", subMainQuery / 1000000000l, subMainQuery / 1000000000l / 60));
        System.out.println(String.format("%-30s %4d s [%2d m]", "    TOTAL time for Headers_Query:", headersQuery / 1000000000l, headersQuery / 1000000000l / 60));
        System.out.println(String.format("%-30s %4d s [%2d m]", "    TOTAL time for Others Query:", otherQuery / 1000000000l, otherQuery / 1000000000l / 60));
        System.out.println(String.format("%-12s %4d s [%2d m]", "total time taken -> paginate:", paginateTime / 1000000000l, (paginateTime / 1000000000l) / 60));
        System.out.println(String.format("%-30s %4d s [%2d m]", "total time taken -> generate:", generateTime / 1000000000l, (generateTime / 1000000000l) / 60));
        //System.out.println(String.format("%46s", "---------------"));
        System.out.println(String.format("%-30s %4d s [%2d m]", "    total time for Process LayOut:", processLayOutTime / 1000000000l, (processLayOutTime / 1000000000l) / 60));
        System.out.println(String.format("%-30s %4d s [%2d m]", "    TOTAL time for processing:", totProceTime / 1000000000l, (totProceTime / 1000000000l) / 60));
        //System.out.println(String.format("%46s", "---------------"));

        br.close();

    }

}

