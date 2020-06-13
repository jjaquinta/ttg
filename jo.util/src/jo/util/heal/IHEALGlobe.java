package jo.util.heal;

import java.util.Iterator;

public interface IHEALGlobe<T>
{
    public int getResolution();
    public int size();
    public void clear();
    public void set(IHEALCoord ord, T val);
    public T get(IHEALCoord ord);
    public Iterator<T> iterator();
    public Iterator<IHEALCoord> coordsIterator();
    public void addGlobeChangeListener(IGlobeChangeListener<T> l);
    public void removeGlobeChangeListener(IGlobeChangeListener<T> l);
}

