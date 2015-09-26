package com.cohen.assaf.emptywords.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 26/09/2015.
 */
public class RunQueue implements Runnable
{
    private List list = new ArrayList();

    public void queue(Runnable task)
    {
        list.add(task);
    }

    public void run()
    {
        while(list.size() > 0)
        {
            Runnable task = (Runnable) list.get(0);

            list.remove(0);
            task.run();
        }
    }
}
