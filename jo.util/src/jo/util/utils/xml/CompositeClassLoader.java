package jo.util.utils.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * ClassLoader that is composed of other classloaders. Each loader will be used
 * to try to load the particular class, until one of them succeeds. <b>Note:</b>
 * The loaders will always be called in the REVERSE order they were added in.
 * 
 * <p>
 * The Composite class loader also has registered the classloader that loaded
 * xstream.jar and (if available) the thread's context classloader.
 * </p>
 * 
 * <h1>Example</h1>
 * 
 * <pre><code>
 * CompositeClassLoader loader = new CompositeClassLoader();
 * loader.add(MyClass.class.getClassLoader());
 * loader.add(new AnotherClassLoader());
 * 
 * loader.loadClass(&quot;com.blah.ChickenPlucker&quot;);
 * </code></pre>
 * 
 * <p>
 * The above code will attempt to load a class from the following classloaders
 * (in order):
 * </p>
 * 
 * <ul>
 * <li>AnotherClassLoader (and all its parents)</li>
 * <li>The classloader for MyClas (and all its parents)</li>
 * <li>The thread's context classloader (and all its parents)</li>
 * <li>The classloader for XStream (and all its parents)</li>
 * </ul>
 * 
 * @author Joe Walnes
 * @since 1.0.3
 */
public class CompositeClassLoader extends ClassLoader
{

    private final List<ClassLoader> classLoaders = Collections
                                            .synchronizedList(new ArrayList<ClassLoader>());

    public CompositeClassLoader()
    {
        add(Object.class.getClassLoader()); // bootstrap loader.
        add(getClass().getClassLoader());   // whichever classloader loaded this
                                            // jar.
    }

    /**
     * Add a loader to the n
     * 
     * @param classLoader
     */
    public void add(ClassLoader classLoader)
    {
        if ((classLoader != null) && !classLoaders.contains(classLoader)
                && (classLoader != this))
        {
            //System.err.println("Adding " + classLoader.getClass().getName());
            classLoaders.add(0, classLoader);
        }
    }
    
    public void remove(ClassLoader classLoader)
    {
        if (classLoader != null)
            classLoaders.remove(classLoader);
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException
    {
        //System.err.println("Loading " + name);
        for (ClassLoader classLoader : classLoaders)
        {
            try
            {
                Class<?> ret = classLoader.loadClass(name);
                //System.err.println("  found in " + classLoader.getClass().getName());
                return ret;
            }
            catch (ClassNotFoundException notFound)
            {
                //System.err.println("  not in " + classLoader.getClass().getName());
                // ok.. try another one
            }
        }
        // One last try - the context class loader associated with the current
        // thread. Often used in j2ee servers.
        // Note: The contextClassLoader cannot be added to the classLoaders list
        // up front as the thread that constructs
        // XStream is potentially different to thread that uses it.
        ClassLoader contextClassLoader = Thread.currentThread()
                .getContextClassLoader();
        if ((contextClassLoader != null)
                && !classLoaders.contains(contextClassLoader)
                && (contextClassLoader != this))
        {
            Class<?> ret = contextClassLoader.loadClass(name);
            //System.err.println("  found in " + contextClassLoader.getClass().getName());
            return ret;
        }
        else
        {
            //System.err.println("  not in " + contextClassLoader.getClass().getName());
            System.err.println("  we have failed!");
            throw new ClassNotFoundException(name);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.ClassLoader#findClass(java.lang.String)
     */
    protected Class<?> findClass(String arg0) throws ClassNotFoundException
    {
        System.err.println("findClass(String arg0)");
        return super.findClass(arg0);
    }

    /* (non-Javadoc)
     * @see java.lang.ClassLoader#findLibrary(java.lang.String)
     */
    protected String findLibrary(String arg0)
    {
        System.err.println("findLibrary(String arg0)");

        return super.findLibrary(arg0);
    }

    /* (non-Javadoc)
     * @see java.lang.ClassLoader#findResource(java.lang.String)
     */
    protected URL findResource(String arg0)
    {
        System.err.println("findResource(String arg0)");

        return super.findResource(arg0);
    }

    /* (non-Javadoc)
     * @see java.lang.ClassLoader#findResources(java.lang.String)
     */
    protected Enumeration<URL> findResources(String arg0) throws IOException
    {
        System.err.println("findResources(String arg0)");

        return super.findResources(arg0);
    }

    /* (non-Javadoc)
     * @see java.lang.ClassLoader#getPackage(java.lang.String)
     */
    protected Package getPackage(String arg0)
    {
        System.err.println("getPackage(String arg0)");

        return super.getPackage(arg0);
    }

    /* (non-Javadoc)
     * @see java.lang.ClassLoader#getPackages()
     */
    protected Package[] getPackages()
    {
        System.err.println("getPackages()");

        return super.getPackages();
    }

    /* (non-Javadoc)
     * @see java.lang.ClassLoader#getResource(java.lang.String)
     */
    public URL getResource(String arg0)
    {
        System.err.println("getResource(String arg0)");

        return super.getResource(arg0);
    }

    /* (non-Javadoc)
     * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream(String arg0)
    {
        System.err.println("getResourceAsStream(String arg0)");

        return super.getResourceAsStream(arg0);
    }

    /* (non-Javadoc)
     * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
     */
    protected synchronized Class<?> loadClass(String arg0, boolean arg1) throws ClassNotFoundException
    {
        System.err.println("loadClass(String arg0, boolean arg1)");

        return super.loadClass(arg0, arg1);
    }

}
