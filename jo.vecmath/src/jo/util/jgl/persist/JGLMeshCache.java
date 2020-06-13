package jo.util.jgl.persist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jo.util.utils.ArrayUtils;

public class JGLMeshCache
{
    private static Map<Integer, JGLMesh>   mIDToMesh = new HashMap<Integer, JGLMesh>();
    private static Set<Integer> mMRUMesh = new HashSet<Integer>();

    public static void markMesh()
    {
        synchronized (mIDToMesh)
        {
            mMRUMesh.clear();
        }
    }
    
    public static void useMesh(int meshID)
    {
        synchronized (mIDToMesh)
        {
            if (meshID == 0)
                return;
            mMRUMesh.add(meshID);
        }
    }

    public static void useMesh(JGLMesh mesh)
    {
        useMesh(mesh.getMeshID());
    }
    
    public static int[] getUsedMeshes()
    {
        synchronized (mIDToMesh)
        {
            return ArrayUtils.toIntArray(mMRUMesh);
        }
    }
    
    public static int[] getUnUsedMeshes()
    {
        Set<Integer> unused = new HashSet<Integer>();
        synchronized (mIDToMesh)
        {
            unused.addAll(mIDToMesh.keySet());
            unused.removeAll(mMRUMesh);
        }
        return ArrayUtils.toIntArray(unused);
    }

    public static JGLMesh getMesh(int meshID)
    {
        return mIDToMesh.get(meshID);
    }

    public static void registerMesh(int id, JGLMesh mesh)
    {
        synchronized (mIDToMesh)
        {
            mMRUMesh.add(id);
            mIDToMesh.put(id, mesh);
        }
    }

    public static void unregisterMesh(int id)
    {
        synchronized (mIDToMesh)
        {
            mMRUMesh.remove(id);
            mIDToMesh.remove(id);
        }
    }

    public static void unregisterMesh(JGLMesh mesh)
    {
        unregisterMesh(mesh.getMeshID());
    }
}
