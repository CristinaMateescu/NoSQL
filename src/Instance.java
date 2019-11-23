

import java.util.ArrayList;
import java.lang.Math;
import java.text.DecimalFormat;

/**
 *
 * @author Cristina-Ramona
 */
public class Instance implements Comparable<Instance> {

    int nr_atribute;
    ArrayList<Object> values;
    String entity_name;
    long timestamp;

    /**
     *
     */
    public Instance() {
    }

    /**
     * Create Instance, add Fields
     * @param nr_atribute
     * @param entity_name
     * @param parts Fields Array
     * @param db Database
     */
    public Instance(int nr_atribute, String entity_name, String parts[], Database db) {
        this.nr_atribute = nr_atribute;
        values = new ArrayList<>();
        this.entity_name = entity_name;
        timestamp = (long) System.nanoTime();

        //Add Attributes
        int i = 0;
        while (i < nr_atribute) {
            Object value = null;

            if (db.getEntity(entity_name).types.get(i).equals("Integer")) {
                value = new Integer(parts[i + 2]);
            } else if (db.getEntity(entity_name).types.get(i).equals("Float")) {
                value = new Float(parts[i + 2]);
            } else if (db.getEntity(entity_name).types.get(i).equals("String")) {
                value = new String(parts[i + 2]);
            }
            values.add(value);
            i++;

        }

    }

    @Override
    public int compareTo(Instance o) {
        if (timestamp < o.timestamp) {
            return 1;
        } else if (timestamp > o.timestamp) {
            return -1;
        } else {
            return 0;
        }

    }

    /**
     * Add Value in Instance
     * @param o Field Value
     */
    public void add(Object o) {
        values.add(o);
    }
    
    /**
     * Print Instance Fields Values
     * @param db Database
     */
    public void printValues(Database db) {

        Entity entity = db.getEntity(entity_name);
        System.out.print(this.entity_name);
	DecimalFormat df2 = new DecimalFormat("#.##");

        //For each Attribute
        for (int i = 0; i < this.nr_atribute; i++) {
            
                Object atribut = this.values.get(i);
                if ( atribut instanceof Float  &&
                     Math.round((float)atribut) == (float)atribut  )
                    
                    System.out.print(" " + entity.fields.get(i) + ":"
                                         + Math.round((float)atribut));

                else if ( atribut instanceof Float ) 
                    System.out.print(" " + entity.fields.get(i) + ":"
                                         + df2.format(atribut));

		else   

                    System.out.print(" " + entity.fields.get(i) + ":"
                                         + atribut);
            
        }

        System.out.print('\n');
    }

    /**
     * Update Instance Fields
     * @param entity
     * @param parts Array of Attributes
     */
    public void update(Entity entity, String[] parts) {
        
        for ( int i=0; i < (parts.length-3)/2; i++ )
        {
            int iAtribut = entity.getAtributeIndex(parts[i+3]);
            String type = entity.types.get(iAtribut);
            Object newValue=null;
            
            if (type.equals("Integer")) {
                newValue = new Integer(parts[i + 4]);
            } else if (type.equals("Float")) {
                newValue = new Float(parts[i + 4]);
            } else if (type.equals("String")) {
                newValue = new String(parts[i + 4]);
            }
            
            values.set(iAtribut, newValue);
            timestamp = (long) System.nanoTime();
            
        }
    }

    
}
