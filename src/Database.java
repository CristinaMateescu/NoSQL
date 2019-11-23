
import java.util.ArrayList;
import java.util.Collections;


/**
 *
 * @author Cristina-Ramona
 */
public class Database {

    ArrayList<Entity> entities;
    ArrayList<Nod> noduri;
    int numar_noduri;
    int capacitate_noduri;
    String name;

    /**
     * Constructor fara parametri
     */
    public Database() {
        
        this.entities = new ArrayList<>();
        this.noduri = new ArrayList<>();
        this.numar_noduri = 0;
        this.capacitate_noduri = 0;
    }

    /**
     * Set DB-parameters, add Nodes
     * @param name DB name
     * @param numar_noduri No. of Nodes
     * @param capacitate_noduri Capacity of Nodes
     */
    public void setDB(String name, int numar_noduri, int capacitate_noduri) {

        this.name = name;
        this.numar_noduri = numar_noduri;
        this.capacitate_noduri = capacitate_noduri;

        int i = 0;
        while (i < numar_noduri) {

            Nod nod = new Nod(capacitate_noduri);
            noduri.add(nod);
            i++;
        }
    }

    /**
     * Add Entity
     * @param e Entity to be added 
     */
    public void add_entity(Entity e) {

        entities.add(e);
    }

    /**
     * Gets the next Node for Instance Insert
     * @param inst Instance 
     * @return Node index where Instance can be inserted
     */
    public int getNodeToInsert(Instance inst) {

        int max_index = -1;
        int max_dimension = -1;

        for (int i = 0; i < numar_noduri; i++) {

            Nod nod = noduri.get(i);
            if (!nod.containsInstance(inst)
                    && max_dimension < nod.dimensiune
                    && nod.capacitate > nod.dimensiune) {

                max_dimension = nod.dimensiune;
                max_index = i;
            }
        }
        return max_index;
    }

    /**
     * Get Entity by name
     * @param name Entity name
     * @return Entity of a given name
     */
    public Entity getEntity(String name) {

        for (int i = 0; i < entities.size(); i++) {
            if (name.equals(entities.get(i).nume)) {

                return entities.get(i);
            }
        }
        return null;
    }

    /**
     * Insert an Instance (parameter) into a Node (index)
     * @param index_node The Index of a certain Node
     * @param instance an Instance
     */
    public void insertInstanceInNode(int index_node, Instance instance) {
        
        noduri.get(index_node).instances.add(0, instance);
        noduri.get(index_node).dimensiune++;
    }

    /**
     * Database Snapshot
     */
    public void snapshotdb() {

        int i_nod = 0;
        boolean exists_Nonempty_nodes = false;

        //For each  non-empty node 
        while (i_nod < numar_noduri && noduri.get(i_nod).dimensiune != 0) {

            exists_Nonempty_nodes = true;
            System.out.println("Nod" + (i_nod + 1));
            Nod nod = noduri.get(i_nod);
            nod.printInstances(this);

            i_nod++;
        }
        if (!exists_Nonempty_nodes) {
            
            System.out.println("EMPTY DB");
        }
    }

    /**
     * Delete all Instances with Primary Key - key 
     * @param entity_name 
     * @param key Primary Key of Entity
     */
    public void delete(String entity_name, String key) {
        
        Entity entity = getEntity(entity_name);
        int RF = entity.RF;

        //For each node
        for ( int i_node =0 ; i_node < numar_noduri && RF > 0; i_node++) {

            int i_instance = 0;
            while (i_instance < noduri.get(i_node).dimensiune && RF > 0) {

                Instance instance = noduri.get(i_node).instances.get(i_instance);
                
                if (instance.entity_name.equals(entity_name)
                      && String.valueOf(instance.values.get(0)).equals(key)) {

                    noduri.get(i_node).instances.remove(i_instance);
                    noduri.get(i_node).dimensiune--;
                    RF--;
                    i_instance--;
                }
                i_instance++;
            }
        }
        if (RF > 0) {
            System.out.println("NO INSTANCE TO DELETE");
        }
    }

    /**
     * Print Instances with specific format
     * @param entity_name
     * @param key Primary Key
     */
    public void printInstance(String entity_name, String key) {

        Entity entity = getEntity(entity_name);
        int RF = entity.RF;

        //Parcurgere noduri
        for (int i_node=0; i_node < numar_noduri && RF > 0; i_node++) {

            int i_instance = 0;
            while (i_instance < noduri.get(i_node).dimensiune && RF > 0) {

                Instance instance = noduri.get(i_node).instances.get(i_instance);
                
                if (instance.entity_name.equals(entity_name)
                    && String.valueOf(instance.values.get(0)).equals(key)) {

                    System.out.print("Nod" + (i_node + 1) + " ");
                    if (RF == 1) 
                        instance.printValues(this);  
                    RF--;
                }
                i_instance++;
            }
        }
        if (RF > 0) {
            System.out.println("NO INSTANCE FOUND");
        }

    }

    /**
     * Update certain fields of Instance
     * @param entity_name
     * @param key Primary Key
     * @param parts Array of attributes
     */
    public void update(String entity_name, String key, String[] parts) {
        Entity entity = getEntity(entity_name);
        int RF = entity.RF;

        //For each node, each Instance, search Instance
        for (int iNod=0; iNod < numar_noduri && RF > 0; iNod++) {
            for (int iInst=0; iInst < noduri.get(iNod).dimensiune && RF > 0;iInst++) {

                Instance instance = noduri.get(iNod).instances.get(iInst);
                
                if (instance.entity_name.equals(entity_name)
                    && String.valueOf(instance.values.get(0)).equals(key)) {
                    
                    instance.update(entity,parts);
                    Collections.sort(noduri.get(iNod).instances);
                    RF--;
                }
            }
        }   
    }

    /**
     * Cleanup Database
     * @param timestamp 
     */
    public void cleanup(long timestamp) {
    
        for ( int iNod=0; iNod<numar_noduri; iNod++)
        {
            int OldInstance = noduri.get(iNod).getOldInstance(timestamp);
            while (OldInstance != -1)
            {
                Instance instance = noduri.get(iNod).instances.get(OldInstance);
                delete(instance.entity_name, String.valueOf (instance.values.get(0)));
		OldInstance = noduri.get(iNod).getOldInstance(timestamp);
            }
        }
    }

    /**
     * In case of full DB, add another Node
     */
    public void addNode() {
        
        Nod nod = new Nod(capacitate_noduri);
        numar_noduri++;
        noduri.add(nod);
    }
}
        
