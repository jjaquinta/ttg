/**
 * Created on Sep 20, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package jo.ttg.beans.sys;

import java.util.ArrayList;
import java.util.List;

import jo.util.beans.Bean;

/**
 * Class for Animals Table
 *
 * This class encapsulates the animal encounter table for a world.
 * Animal encounters are generated for each position for all
 * terrain types.
 * Animals are generated from a TMainWorld class.
 *
 * @author  Joseph Jaquinta
 * @version     3.0, 01 July, 2001
 * @see ttg.TAnimal
 * @see ttg.TMainWorld#GenerateAnimals
 * */
public class AnimalsBean extends Bean
{
    private List<AnimalBean> mAnimals;

    public AnimalsBean()
    {
        mAnimals = new ArrayList<AnimalBean>();
    }

    public void setAnimals(AnimalBean[] list)
    {
        mAnimals.clear();
        if (list != null)
            for (int i = 0; i < list.length; i++)
                mAnimals.add(list[i]);
    }

    public void setAnimals(AnimalBean element, int position)
    {
        mAnimals.set(position, element);
    }

    public AnimalBean[] getAnimals()
    {
        return mAnimals.toArray(new AnimalBean[0]);
    }

    public AnimalBean getAnimals(int position)
    {
        return mAnimals.get(position);
    }
}
