

import java.util.ArrayList;

/**
 *
 * @author Cristina-Ramona
 */
public class Entity {

    int RF;
    int nr_atribute;
    String nume;
    ArrayList<String> fields;
    ArrayList<String> types;

    /**
     * 
     */
    public Entity() {

    }

    /**
     * Constructor
     * @param nume
     * @param RF
     * @param nr_atribute
     * @param parts Array of Attributes
     */
    public Entity(String nume, int RF, int nr_atribute, String parts[]) {

        this.nr_atribute = nr_atribute;
        this.nume = nume;
        this.RF = RF;
        this.fields = new ArrayList<>();
        this.types = new ArrayList<>();

        //Add fields
        int i = 1;
        while (i < 2 * nr_atribute) {

            add_field(parts[3 + i], parts[3 + i + 1]);
            i += 2;
        }
    }

    /**
     * Add field, with its type
     * @param field Attribute
     * @param type Data type
     */
    public void add_field(String field, String type) {

        fields.add(field);
        types.add(type);
    }

    /**
     * Get the index of an Attribute
     * @param value Attribute as a String
     * @return Attribute index 
     */
    public int getAtributeIndex(String value) {
        
        for ( int i=0; i< nr_atribute; i++ )
        {
            if (String.valueOf(fields.get(i)).equals(value))
                return i;
        }
        
        return -1;
    }
 

}
