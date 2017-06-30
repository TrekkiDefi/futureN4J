package com.github.ittalks.commons.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘春龙 on 2017/5/16.
 *
 * 检查Java虚拟机中运行的线程
 */
public class JvmThreadReporter {

    /**
     * API:
     *      方法简介:
     *          1.activeCount() -- 返回线程组中"活动线程"的估计数
     *          2.activeGroupCount()  -- 返回线程组中"活动线程组"的估计数
     *          3.enumerate(Thread[] list,boolean recurse)  -- 把此线程组中"所有活动线程"复制到指定数组中
     *          4.enumerate(ThreadGroup[] list,boolean recurse)  -- 把此线程组中"所有活动子线程组"的引用复制到指定的数组中
     *          5.enumerate(Thread[] list)  -- 把此线程组中"所有活动线程"复制到指定数组中
     *          6.enumerate(ThreadGroup[] list)  -- 把此线程组中"所有活动子线程组"的引用复制到指定的数组中
     *          7.getName() -- 返回此线程组的名称
     *          8.getParent() -- 返回此线程组的父线程组
     *
     *          注:如果递归标志recurse为true，也包括引用在此线程组的子组的所有活动线程。
     */

    /**
     * 获取根线程组
     * @return
     */
    public static ThreadGroup getRootThreadGroup() {
        //获取当前线程所属的线程组
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();

        //循环获取父级线程组,直至到根线程组
        while (true) {
            if (rootGroup.getParent() != null) {
                rootGroup = rootGroup.getParent();
            } else {
                return rootGroup;
            }
        }
    }

    /**
     * 获取线程组中"子活动线程名"集合
     * @param group 指定的线程组
     * @return
     */
    public static List<String> getThreadNames(ThreadGroup group) {
        List<String> threadNameList = new ArrayList<String>();

        Thread[] threads = new Thread[group.activeCount()];//根据线程组中"活动线程的估计数"创建线程数组
        int count = group.enumerate(threads, false);
        for (int i = 0; i < count; i++) {
            threadNameList.add(threads[i].getName());
        }
        return threadNameList;
    }

    /**
     * 递归获取获取线程组中"子孙活动线程名"集合
     * @param group 指定的线程组
     * @return
     */
    public static List<String> getThreadNamesRecurse(ThreadGroup group) {

        //获取当前线程组下活动线程的名称集合
        List<String> threadNameList = getThreadNames(group);

        ThreadGroup[] groups = new ThreadGroup[group.activeGroupCount()];
        int count = group.enumerate(groups, false);

        for (int i = 0; i < count; i++) {
            threadNameList.addAll(getThreadNames(groups[i]));
        }
        return threadNameList;
    }
}
