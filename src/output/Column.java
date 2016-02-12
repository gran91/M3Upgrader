/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package output;

/**
 *
 * @author Jeremy.CHAUT
 */
public class Column {

    private int idLng;
    private String name;
    private int type;
    private boolean hidden;

    public Column(String n, int ind) {
        build(ind, n, 0, false);
    }

    public Column(String n, int ind, int t) {
        build(ind, n, t, false);
    }

    public Column(String n, int ind, int t, boolean h) {
        build(ind, n, t, h);
    }

    private void build(int i, String n, int typ, boolean hide) {
        idLng = i;
        name = n;
        type = typ;
        hidden = hide;
    }

    public int getIdLng() {
        return idLng;
    }

    public void setIdLng(int id) {
        this.idLng = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
