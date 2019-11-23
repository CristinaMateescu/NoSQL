

import java.util.ArrayList;
import java.text.DecimalFormat;

/**
 *
 * @author Cristina-Ramona
 */
public class Nod {

    int capacitate;
    int dimensiune;
    ArrayList<Instance> instances;

    /**
     *
     */
    public Nod() {
    }

    /**
     *
     * @param capacitate
     */
    public Nod(int capacitate) {
        
        this.instances = new ArrayList<>();
        this.capacitate = capacitate;
        this.dimensiune = 0;
    }

    /**
     * Check if Certain Node contains an Instance
     * @param i Instance
     * @return true or false
     */
    public boolean containsInstance(Instance i) {
        int index = 0;
        while (index < dimensiune) {
            if (instances.get(index).entity_name.equals(i.entity_name)
                    && instances.get(index).values.get(0).equals(i.values.get(0))) {
                return true;
            }
            index++;
        }
        return false;
    }

    /**
     * Print Instances of the Node
     * @param db Database
     */
    public void printInstances(Database db) {
	
	DecimalFormat df2 = new DecimalFormat("#.##");

        //For each instance of the node
        for (int i_instances = 0; i_instances < this.dimensiune; i_instances++) {

            Instance instance = this.instances.get(i_instances);
            Entity entity = db.getEntity(instance.entity_name);
            System.out.print(entity.nume);

            //For each atribute of that instance
            for (int i_atribute = 0; i_atribute < entity.nr_atribute; i_atribute++) {
                
                Object atribut = instance.values.get(i_atribute);
                if ( atribut instanceof Float  &&
                     Math.round((float)atribut) == (float)atribut  )
                    
                    System.out.print(" " + entity.fields.get(i_atribute) + ":"
                                         + Math.round((float)atribut));

		else if ( atribut instanceof Float ) 
                    System.out.print(" " + entity.fields.get(i_atribute) + ":"
                                         + df2.format(atribut));

                else 
                   System.out.print(" " + entity.fields.get(i_atribute) + ":"
                                        + atribut); 
            }
            System.out.print('\n');

        }
    }
    
    /**
     * Return first Instance older than timestamp
     * @param timestamp 
     * @return
     */
    public int getOldInstance(long timestamp)
    {
        for(int i=0; i<dimensiune; i++)
            if(instances.get(i).timestamp < timestamp)
                return i;
        return -1;
    }
}
