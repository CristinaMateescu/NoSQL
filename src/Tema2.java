

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 *
 * @author Cristina-Ramona
 */
public class Tema2 {

    /**
     *
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {

        Scanner scan = new Scanner(new File(args[0]));
        PrintStream fileStream = new PrintStream(args[0] + "_out");
        System.setOut(fileStream);
        Database db = new Database();

        while (scan.hasNext()) {
            String operation = scan.nextLine();
            String parts[] = operation.split(" ");

            if (parts[0].equals("CREATEDB")) {

                //Set database
                db.setDB(parts[1], 		     //name
                        Integer.parseInt(parts[2]),  //numar noduri
                        Integer.parseInt(parts[3])); //capacitate noduri

            } else if (parts[0].equals("CREATE")) {

                //Create entity
                Entity e = new Entity(parts[1],     //name
                        Integer.parseInt(parts[2]), //RF
                        Integer.parseInt(parts[3]), //nr. atribute
                        parts);
                //Add entity in database
                db.add_entity(e);

            } else if (parts[0].equals("INSERT")) {

                String entity_name = parts[1];
                int RF = db.getEntity(entity_name).RF;
                int nr_atribute = db.getEntity(entity_name).nr_atribute;

                while (RF > 0) {
                    //Create instance
                    Instance instance = new Instance(nr_atribute, entity_name, parts, db);

                    //Insert instance in node
                    int node_index = db.getNodeToInsert(instance);
                    
                    //Treat full-DB case
                    if (node_index != -1) {
                        db.insertInstanceInNode(node_index, instance);
                    } else {
                        db.addNode();
                        db.insertInstanceInNode(db.numar_noduri - 1, instance);
                    }
                    RF--;
                }

            } else if (parts[0].equals("SNAPSHOTDB")) {

                db.snapshotdb();

            } else if (parts[0].equals("DELETE")) {

                String entity_name = parts[1];
                String key = parts[2];
                db.delete(entity_name, key);

            } else if (parts[0].equals("GET")) {

                String entity_name = parts[1];
                String key = parts[2];
                db.printInstance(entity_name, key);

            } else if (parts[0].equals("UPDATE")) {

                String entity_name = parts[1];
                String key = parts[2];
                db.update(entity_name, key, parts);

            } else if (parts[0].equals("CLEANUP")) {

                long timestamp = Long.valueOf(parts[2]);
                db.cleanup(timestamp);
            }
        }
    }
}
