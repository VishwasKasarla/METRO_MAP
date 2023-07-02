
import java.util.*;
import java.io.*;


class Graph_M {

    public class Vertex {
        HashMap<String, Integer> connected_stations = new HashMap<>();
    }

    static HashMap<String, Vertex> stations;

    public Graph_M() {
        stations = new HashMap<>();
    }

    // returns total number of stations.
    public int num_of_Stations() {
        return this.stations.size();
    }


    // checks if the station is present on not in the list.
    public boolean containsVertex(String station_name) {
        return this.stations.containsKey(station_name);
    }


    // adds a station to the list.
    public void addVertex(String station_name) {
        Vertex vtx = new Vertex();
        stations.put(station_name, vtx);
    }


    // removes the station from the list.
    public void removeStation(String station_name) {
        Vertex vtx = stations.get(station_name);
        ArrayList<String> keys = new ArrayList<>(vtx.connected_stations.keySet());

        for (String key : keys) {
            Vertex nbrVtx = stations.get(key);
            nbrVtx.connected_stations.remove(station_name);
        }

        stations.remove(station_name);
    }


    // returns total number of connections between each and every station to other stations.
    public int numEdges() {
        ArrayList<String> keys = new ArrayList<>(stations.keySet());
        int count = 0;

        for (String key : keys) {
            Vertex vtx = stations.get(key);
            count = count + vtx.connected_stations.size();
        }

        return count / 2;
    }


    // checks if two stations are connected directly or not.
    public boolean containsEdge(String station_name1, String station_name2) {
        Vertex vtx1 = stations.get(station_name1);
        Vertex vtx2 = stations.get(station_name2);

        if (vtx1 == null || vtx2 == null || !vtx1.connected_stations.containsKey(station_name2)) {
            return false;
        }

        return true;
    }


    // helps to add a connection between two stations.
    public void addEdge(String station_name1, String station_name2, int value) {
        Vertex vtx1 = stations.get(station_name1);
        Vertex vtx2 = stations.get(station_name2);

        if (vtx1 == null || vtx2 == null || vtx1.connected_stations.containsKey(station_name2)) {
            return;
        }

        vtx1.connected_stations.put(station_name2, value);
        vtx2.connected_stations.put(station_name1, value);
    }


    // removes the connection between two stations.
    public void removeEdge(String station_name1, String station_name2) {
        Vertex vtx1 = stations.get(station_name1);
        Vertex vtx2 = stations.get(station_name2);

        //check if the vertices given or the edge between these vertices exist or not
        if (vtx1 == null || vtx2 == null || !vtx1.connected_stations.containsKey(station_name2)) {
            return;
        }

        vtx1.connected_stations.remove(station_name2);
        vtx2.connected_stations.remove(station_name1);
    }


    // this fuction helps to visualize the map created.
    public void display_Map() {
        System.out.println("\t Delhi Metro Map");
        System.out.println("\t------------------");
        System.out.println("----------------------------------------------------\n");
        ArrayList<String> keys = new ArrayList<>(stations.keySet());

        for (String key : keys) {
            String str = key + " =>\n";
            Vertex vtx = stations.get(key);
            ArrayList<String> vtxnbrs = new ArrayList<>(vtx.connected_stations.keySet());

            for (String nbr : vtxnbrs) {
                str = str + "\t" + nbr + "\t";
                if (nbr.length() < 16)
                    str = str + "\t";
                if (nbr.length() < 8)
                    str = str + "\t";
                str = str + vtx.connected_stations.get(nbr) + "\n";
            }
            System.out.println(str);
        }
        System.out.println("\t------------------");
        System.out.println("---------------------------------------------------\n");

    }


    // it displays the list of all the stations in the list.
    public void display_Stations() {
        System.out.println("\n***********************************************************************\n");
        ArrayList<String> keys = new ArrayList<>(stations.keySet());
        int i = 1;
        for (String key : keys) {
            System.out.println(i + ". " + key);
            i++;
        }
        System.out.println("\n***********************************************************************\n");
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // this function helps two check if two stations are connected or not either directly or indirectly.
    public boolean hasPath(String station_name1, String station_name2, HashMap<String, Boolean> processed) {
        // DIR EDGE
        if (containsEdge(station_name1, station_name2)) {
            return true;
        }

        //MARK AS DONE
        processed.put(station_name1, true);

        Vertex vtx = stations.get(station_name1);
        ArrayList<String> nbrs = new ArrayList<>(vtx.connected_stations.keySet());

        //TRAVERSE THE NBRS OF THE VERTEX
        for (String nbr : nbrs) {

            if (!processed.containsKey(nbr))
                if (hasPath(nbr, station_name2, processed))
                    return true;
        }

        return false;
    }


    private class DijkstraPair implements Comparable<DijkstraPair> {
        String vname;
        String psf;
        int cost;

			/*
			The compareTo method is defined in Java.lang.Comparable.
			Here, we override the method because the conventional compareTo method
			is used to compare strings,integers and other primitive data types. But
			here in this case, we intend to compare two objects of DijkstraPair class.
			*/

        /*
        Removing the overriden method gives us this errror:
        The type Graph_M.DijkstraPair must implement the inherited abstract method Comparable<Graph_M.DijkstraPair>.compareTo(Graph_M.DijkstraPair)

        This is because DijkstraPair is not an abstract class and implements Comparable interface which has an abstract
        method compareTo. In order to make our class concrete(a class which provides implementation for all its methods)
        we have to override the method compareTo
         */
        @Override
        public int compareTo(DijkstraPair o) {
            return o.cost - this.cost;
        }
    }


    // this function return the shortest distance between the given stations.
    public int dijkstra(String src, String des, boolean nan) {
        int val = 0;
        ArrayList<String> ans = new ArrayList<>();
        HashMap<String, DijkstraPair> map = new HashMap<>();

        Heap<DijkstraPair> heap = new Heap<>();

        for (String key : stations.keySet()) {
            DijkstraPair new_pair = new DijkstraPair();
            new_pair.vname = key;
            //np.psf = "";
            new_pair.cost = Integer.MAX_VALUE;

            if (key.equals(src)) {
                new_pair.cost = 0;
                new_pair.psf = key;
            }

            heap.add(new_pair);
            map.put(key, new_pair);
        }

        //keep removing the pairs while heap is not empty
        while (!heap.isEmpty()) {
            DijkstraPair removed_pair = heap.remove();

            if (removed_pair.vname.equals(des)) {
                val = removed_pair.cost;
                break;
            }

            map.remove(removed_pair.vname);

            ans.add(removed_pair.vname);

            Vertex v = stations.get(removed_pair.vname);
            for (String nbr : v.connected_stations.keySet()) {
                if (map.containsKey(nbr)) {
                    int old_cost = map.get(nbr).cost;
                    Vertex k = stations.get(removed_pair.vname);
                    int new_cost;
                    if (nan)
                        new_cost = removed_pair.cost + 120 + 40 * k.connected_stations.get(nbr);
                    else
                        new_cost = removed_pair.cost + k.connected_stations.get(nbr);

                    if (new_cost < old_cost) {
                        DijkstraPair gp = map.get(nbr);
                        gp.psf = removed_pair.psf + nbr;
                        gp.cost = new_cost;

                        heap.updatePriorityQueue(gp);
                    }
                }
            }
        }
       // System.out.println(ans);
        return val;
    }


    // blueprint of the Pair data structure.
    private class Pair {
        String vname;
        String psf;
        int min_dis;
        int min_time;
    }


     // this function return shortest path between two nodes in addition with shortest distance.
    public String Get_Minimum_Distance(String src, String dst) {
        int min = Integer.MAX_VALUE;
        String ans = "";
        HashMap<String, Boolean> processed = new HashMap<>();
        LinkedList<Pair> stack = new LinkedList<>();

        // create a new pair
        Pair sp = new Pair();
        sp.vname = src;
        sp.psf = src + "  ";
        sp.min_dis = 0;
        sp.min_time = 0;

        // put the new pair in stack
        stack.addFirst(sp);

        // while stack is not empty keep on doing the work
        while (!stack.isEmpty()) {
            // remove a pair from stack
            Pair rp = stack.removeFirst();

            if (processed.containsKey(rp.vname)) {
                continue;
            }

            // processed put
            processed.put(rp.vname, true);

            //if there exists a direct edge b/w removed pair and destination vertex
            if (rp.vname.equals(dst)) {
                int temp = rp.min_dis;
                if (temp < min) {
                    ans = rp.psf;
                    min = temp;
                }
                continue;
            }

            Vertex rpvtx = stations.get(rp.vname);
            ArrayList<String> nbrs = new ArrayList<>(rpvtx.connected_stations.keySet());

            for (String nbr : nbrs) {
                // process only unprocessed nbrs
                if (!processed.containsKey(nbr)) {

                    // make a new pair of nbr and put in queue
                    Pair np = new Pair();
                    np.vname = nbr;
                    np.psf = rp.psf + nbr + "  ";
                    np.min_dis = rp.min_dis + rpvtx.connected_stations.get(nbr);
                    stack.addFirst(np);
                }
            }
        }
        ans = ans + Integer.toString(min);
        return ans;
    }



    // this function return shortest path between two nodes in addition with minimum time required.
    public String Get_Minimum_Time(String src, String dst) {
        int min = Integer.MAX_VALUE;
        String ans = "";
        HashMap<String, Boolean> processed = new HashMap<>();
        LinkedList<Pair> stack = new LinkedList<>();

        // create a new pair
        Pair sp = new Pair();
        sp.vname = src;
        sp.psf = src + "  ";
        sp.min_dis = 0;
        sp.min_time = 0;

        // put the new pair in queue
        stack.addFirst(sp);

        // while queue is not empty keep on doing the work
        while (!stack.isEmpty()) {

            // remove a pair from queue
            Pair rp = stack.removeFirst();

            if (processed.containsKey(rp.vname)) {
                continue;
            }

            // processed put
            processed.put(rp.vname, true);

            //if there exists a direct edge b/w removed pair and destination vertex
            if (rp.vname.equals(dst)) {
                int temp = rp.min_time;
                if (temp < min) {
                    ans = rp.psf;
                    min = temp;
                }
                continue;
            }

            Vertex rpvtx = stations.get(rp.vname);
            ArrayList<String> nbrs = new ArrayList<>(rpvtx.connected_stations.keySet());

            for (String nbr : nbrs) {
                // process only unprocessed nbrs
                if (!processed.containsKey(nbr)) {

                    // make a new pair of nbr and put in queue
                    Pair np = new Pair();
                    np.vname = nbr;
                    np.psf = rp.psf + nbr + "  ";

                    np.min_time = rp.min_time + 120 + 40 * rpvtx.connected_stations.get(nbr);  // 40 seconds for 1 km plus 120 seconds at each station (some delay or any kind of stopage)
                    stack.addFirst(np);
                }
            }
        }
        Double minutes = Math.ceil((double) min / 60);
        ans = ans + Double.toString(minutes);
        return ans;
    }


    /* this function returns where train needs(means we reqiure more than one train to reach destination) to get changed and its count.
    if station code(after '~' sign) is of length of 2 (e.g. "Rajouri Garden~BP" , code is BP ) if the station previous and next to it have code of length 1,
    than this means current station is a interchanging station.
    */
    public ArrayList<String> get_Interchanges(String str) {
        ArrayList<String> arr = new ArrayList<>();
        String[] res = str.split("  ");
        arr.add(res[0]);
        int count = 0;
        for (int i = 1; i < res.length - 1; i++) {
            int index = res[i].indexOf('~');
            String s = res[i].substring(index + 1);

            if (s.length() == 2) {
                String prev = res[i - 1].substring(res[i - 1].indexOf('~') + 1);
                String next = res[i + 1].substring(res[i + 1].indexOf('~') + 1);

                if (prev.equals(next)) {
                    arr.add(res[i]);
                } else {
                    arr.add(res[i] + " ==> " + res[i + 1]);
                    i++;
                    count++;
                }
            } else {
                arr.add(res[i]);
            }
        }
        arr.add(Integer.toString(count));
        arr.add(res[res.length - 1]);
        return arr;
    }


    // this fuction creates the map web.
    public static void Create_Metro_Map(Graph_M g) {
        g.addVertex("Noida Sector 62~B");
        g.addVertex("Botanical Garden~B");
        g.addVertex("Yamuna Bank~B");
        g.addVertex("Rajiv Chowk~BY");
        g.addVertex("Vaishali~B");
        g.addVertex("Moti Nagar~B");
        g.addVertex("Janak Puri West~BO");
        g.addVertex("Dwarka Sector 21~B");
        g.addVertex("Huda City Center~Y");
        g.addVertex("Saket~Y");
        g.addVertex("Vishwavidyalaya~Y");
        g.addVertex("Chandni Chowk~Y");
        g.addVertex("New Delhi~YO");
        g.addVertex("AIIMS~Y");
        g.addVertex("Shivaji Stadium~O");
        g.addVertex("DDS Campus~O");
        g.addVertex("IGI Airport~O");
        g.addVertex("Rajouri Garden~BP");
        g.addVertex("Netaji Subhash Place~PR");
        g.addVertex("Punjabi Bagh West~P");

        g.addEdge("Noida Sector 62~B", "Botanical Garden~B", 8);
        g.addEdge("Botanical Garden~B", "Yamuna Bank~B", 10);
        g.addEdge("Yamuna Bank~B", "Vaishali~B", 8);
        g.addEdge("Yamuna Bank~B", "Rajiv Chowk~BY", 6);
        g.addEdge("Rajiv Chowk~BY", "Moti Nagar~B", 9);
        g.addEdge("Moti Nagar~B", "Janak Puri West~BO", 7);
        g.addEdge("Janak Puri West~BO", "Dwarka Sector 21~B", 6);
        g.addEdge("Huda City Center~Y", "Saket~Y", 15);
        g.addEdge("Saket~Y", "AIIMS~Y", 6);
        g.addEdge("AIIMS~Y", "Rajiv Chowk~BY", 7);
        g.addEdge("Rajiv Chowk~BY", "New Delhi~YO", 1);
        g.addEdge("New Delhi~YO", "Chandni Chowk~Y", 2);
        g.addEdge("Chandni Chowk~Y", "Vishwavidyalaya~Y", 5);
        g.addEdge("New Delhi~YO", "Shivaji Stadium~O", 2);
        g.addEdge("Shivaji Stadium~O", "DDS Campus~O", 7);
        g.addEdge("DDS Campus~O", "IGI Airport~O", 8);
        g.addEdge("Moti Nagar~B", "Rajouri Garden~BP", 2);
        g.addEdge("Punjabi Bagh West~P", "Rajouri Garden~BP", 2);
        g.addEdge("Punjabi Bagh West~P", "Netaji Subhash Place~PR", 3);
        g.addEdge("Rajiv Chowk~BY", "Punjabi Bagh West~P", 1);
    }


    /* this function creates the code_list of the stations.
    e.g. : Dwarka Sector 21~B	---> 	DS21
           Yamuna Bank~B		--->    YB
           Saket~Y			    --->    SA
    */

    public static String[] printCodelist() {
        System.out.println("List of station along with their codes:\n");
        ArrayList<String> keys = new ArrayList<>(stations.keySet());
        int i = 1, j = 0, m = 1;
        StringTokenizer station_name;
        String temp = "";
        String codes[] = new String[keys.size()];
        char c;
        for (String key : keys) {
            station_name = new StringTokenizer(key);
            codes[i - 1] = "";
            j = 0;
            while (station_name.hasMoreTokens()) {
                temp = station_name.nextToken();
                c = temp.charAt(0);
                while (c > 47 && c < 58) {
                    codes[i - 1] += c;
                    j++;
                    c = temp.charAt(j);
                }
                if ((c < 48 || c > 57) && c < 123)
                    codes[i - 1] += c;
            }
            if (codes[i - 1].length() < 2)
                codes[i - 1] += Character.toUpperCase(temp.charAt(1));

            System.out.print(i + ". " + key + "\t");
            if (key.length() < (22 - m))
                System.out.print("\t");
            if (key.length() < (14 - m))
                System.out.print("\t");
            if (key.length() < (6 - m))
                System.out.print("\t");
            System.out.println(codes[i - 1]);
            i++;
            if (i == (int) Math.pow(10, m))
                m++;
        }
        return codes;
    }

    // MAIN FUNCTION.
    public static void main(String[] args) throws IOException
    {
        Graph_M g = new Graph_M();
        Create_Metro_Map(g);

        System.out.println("\n\t\t\t****WELCOME TO THE METRO APP*****");

        BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));


        //STARTING SWITCH CASE
        while(true)
        {
            System.out.println("\t\t\t\t~~LIST OF ACTIONS~~\n\n");
            System.out.println("1. LIST ALL THE STATIONS IN THE MAP");
            System.out.println("2. SHOW THE METRO MAP");
            System.out.println("3. GET SHORTEST DISTANCE FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
            System.out.println("4. GET SHORTEST TIME TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
            System.out.println("5. GET SHORTEST PATH (DISTANCE WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
            System.out.println("6. GET SHORTEST PATH (TIME WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
            System.out.println("7. EXIT THE MENU");
            System.out.print("\nENTER YOUR CHOICE FROM THE ABOVE LIST (1 to 7) : ");
            int choice = -1;

            try {
                choice = Integer.parseInt(inp.readLine());
            } catch(Exception e) {
                System.out.println("ERROR OCCURED ------> " + e);
            }
            System.out.print("\n***********************************************************\n");

            if(choice == 7)
            {
                System.exit(0);
            }
            switch(choice)
            {
                case 1:
                    g.display_Stations();
                    break;

                case 2:
                    g.display_Map();
                    break;

                case 3:
                    ArrayList<String> keys = new ArrayList<>(stations.keySet());
                    String codes[] = printCodelist();
                    System.out.println("\n1. TO ENTER SERIAL NO. OF STATIONS\n2. TO ENTER CODE OF STATIONS\n3. TO ENTER NAME OF STATIONS\n");
                    System.out.println("ENTER YOUR CHOICE:");
                    int ch = Integer.parseInt(inp.readLine());
                    int j;

                    String st1 = "", st2 = "";
                    System.out.println("ENTER THE SOURCE AND DESTINATION STATIONS");
                    if (ch == 1)
                    {
                        st1 = keys.get(Integer.parseInt(inp.readLine())-1);
                        st2 = keys.get(Integer.parseInt(inp.readLine())-1);
                    }
                    else if (ch == 2)
                    {
                        String a,b;
                        a = (inp.readLine()).toUpperCase();
                        for (j=0;j<keys.size();j++)
                            if (a.equals(codes[j]))
                                break;
                        st1 = keys.get(j);
                        b = (inp.readLine()).toUpperCase();
                        for (j=0;j<keys.size();j++)
                            if (b.equals(codes[j]))
                                break;
                        st2 = keys.get(j);
                    }
                    else if (ch == 3)
                    {
                        st1 = inp.readLine();
                        st2 = inp.readLine();
                    }
                    else
                    {
                        System.out.println("Invalid choice");
                        System.exit(0);
                    }

                    HashMap<String, Boolean> processed = new HashMap<>();
                    if(!g.containsVertex(st1) || !g.containsVertex(st2) || !g.hasPath(st1, st2, processed))
                        System.out.println("THE INPUTS ARE INVALID");
                    else
                        System.out.println("SHORTEST DISTANCE FROM "+st1+" TO "+st2+" IS "+g.dijkstra(st1, st2, false)+"KM\n");


                  //  System.out.println(Arrays.toString(codes));
                    break;

                case 4:
                    System.out.print("ENTER THE SOURCE STATION: ");
                    String sat1 = inp.readLine();
                    System.out.print("ENTER THE DESTINATION STATION: ");
                    String sat2 = inp.readLine();

                    HashMap<String, Boolean> processed1= new HashMap<>();
                    System.out.println("SHORTEST TIME FROM ("+sat1+") TO ("+sat2+") IS "+g.dijkstra(sat1, sat2, true)/60+" MINUTES\n\n");
                    break;

                case 5:
                    System.out.println("ENTER THE SOURCE AND DESTINATION STATIONS");
                    String s1 = inp.readLine();
                    String s2 = inp.readLine();

                    HashMap<String, Boolean> processed2 = new HashMap<>();
                    if(!g.containsVertex(s1) || !g.containsVertex(s2) || !g.hasPath(s1, s2, processed2))
                        System.out.println("THE INPUTS ARE INVALID");
                    else
                    {
                        String s = g.Get_Minimum_Distance(s1, s2);
                        System.out.println(s);
                        ArrayList<String> str = g.get_Interchanges(s);

                        int len = str.size();
                        System.out.println("SOURCE STATION : " + s1);
                        System.out.println("SOURCE STATION : " + s2);
                        System.out.println("DISTANCE : " + str.get(len-1));
                        System.out.println("NUMBER OF INTERCHANGES : " + str.get(len-2));
                        //System.out.println(str);
                        System.out.println("~~~~~~~~~~~~~");
                        System.out.println("START  ==>  " + str.get(0));
                        for(int i=1; i<len-3; i++)
                        {
                            System.out.println(str.get(i));
                        }
                        System.out.print(str.get(len-3) + "   ==>    END");
                        System.out.println("\n~~~~~~~~~~~~~");
                    }
                    break;

                case 6:
                    System.out.print("ENTER THE SOURCE STATION: ");
                    String ss1 = inp.readLine();
                    System.out.print("ENTER THE DESTINATION STATION: ");
                    String ss2 = inp.readLine();

                    HashMap<String, Boolean> processed3 = new HashMap<>();
                    if(!g.containsVertex(ss1) || !g.containsVertex(ss2) || !g.hasPath(ss1, ss2, processed3))
                        System.out.println("THE INPUTS ARE INVALID");
                    else
                    {
                        ArrayList<String> str = g.get_Interchanges(g.Get_Minimum_Time(ss1, ss2));
                        int len = str.size();
                        System.out.println("SOURCE STATION : " + ss1);
                        System.out.println("DESTINATION STATION : " + ss2);
                        System.out.println("TIME : " + str.get(len-1)+" MINUTES");
                        System.out.println("NUMBER OF INTERCHANGES : " + str.get(len-2));
                        //System.out.println(str);
                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        System.out.print("START  ==>  " + str.get(0) + " ==>  ");
                        for(int i=1; i<len-3; i++)
                        {
                            System.out.println(str.get(i));
                        }
                        System.out.print(str.get(len-3) + "   ==>    END");
                        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    }
                    break;
                default:  //If switch expression does not match with any case,
                    //default statements are executed by the program.
                    //No break is needed in the default case
                    System.out.println("Please enter a valid option! ");
                    System.out.println("The options you can choose are from 1 to 7. ");

            }
        }

    }
}
