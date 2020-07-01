package jo.util.utils;

import jo.util.beans.PCSBean;

public class ThreadHelper extends Thread
{
    private ThreadHelperDetails mDetails = new ThreadHelperDetails();
    
    // globals

    public static void snooze(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
        }
    }

    public static ThreadHelper currentThreadHelper()
    {
        Thread t = Thread.currentThread();
        if (t instanceof ThreadHelper)
            return (ThreadHelper)t;
        return null;
    }
    
    public static boolean isCanceled()
    {
        ThreadHelper t = currentThreadHelper();
        if (t != null)
            return t.getDetails().isCanceled();
        return false;
    }
    public static void setCanCancel(boolean canCancel)
    {
        ThreadHelper t = currentThreadHelper();
        if (t != null)
            t.getDetails().setCanCancel(canCancel);
    }
    public static void setSubTask(String subTask)
    {
        ThreadHelper t = currentThreadHelper();
        if (t != null)
            t.getDetails().setSubTask(subTask);
    }
    public static void setTotalUnits(int totalUnits)
    {
        ThreadHelper t = currentThreadHelper();
        if (t != null)
            t.getDetails().setTotalUnits(totalUnits);
    }
    public static void work(int units)
    {
        ThreadHelper t = currentThreadHelper();
        if (t != null)
            t.addWork(units);
    }

    // constructors

    public ThreadHelper()
    {
        super();
    }

    public ThreadHelper(Runnable target, String name)
    {
        super(target, name);
    }

    public ThreadHelper(Runnable target)
    {
        super(target);
    }

    public ThreadHelper(String name)
    {
        super(name);
    }

    // utilities
    public void addWork(int units)
    {
        mDetails.setUnitsWorked(mDetails.getUnitsWorked() + units);
    }

    // getters and setters

    public ThreadHelperDetails getDetails()
    {
        return mDetails;
    }

    public void setDetails(ThreadHelperDetails details)
    {
        mDetails = details;
    }
    
    public class ThreadHelperDetails extends PCSBean
    {
        private boolean               mCanceled;
        private boolean               mCanCancel;
        private String                mSubTask;
        private int                   mTotalUnits;
        private int                   mUnitsWorked;

        // getters and setters
    
        public boolean isCanceled()
        {
            return mCanceled;
        }
    
        public void setCanceled(boolean canceled)
        {
            queuePropertyChange("canceled", mCanceled, canceled);
            mCanceled = canceled;
            firePropertyChange();
        }
    
        public String getSubTask()
        {
            return mSubTask;
        }
    
        public void setSubTask(String subTask)
        {
            queuePropertyChange("subTask", mSubTask, subTask);
            mSubTask = subTask;
            firePropertyChange();
        }
    
        public int getTotalUnits()
        {
            return mTotalUnits;
        }
    
        public void setTotalUnits(int totalUnits)
        {
            queuePropertyChange("totalUnits", mTotalUnits, totalUnits);
            mTotalUnits = totalUnits;
            firePropertyChange();
        }
    
        public int getUnitsWorked()
        {
            return mUnitsWorked;
        }
    
        public void setUnitsWorked(int unitsWorked)
        {
            queuePropertyChange("unitsWorked", mUnitsWorked, unitsWorked);
            mUnitsWorked = unitsWorked;
            firePropertyChange();
        }
    
        public boolean isCanCancel()
        {
            return mCanCancel;
        }
    
        public void setCanCancel(boolean canCancel)
        {
            queuePropertyChange("canCancel", mCanCancel, canCancel);
            mCanCancel = canCancel;
            firePropertyChange();
        }
    }

}
