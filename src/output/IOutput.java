/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package output;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Jeremy.CHAUT
 */
public interface IOutput {
    public void write() throws FileNotFoundException, IOException;
    public boolean save();
}
