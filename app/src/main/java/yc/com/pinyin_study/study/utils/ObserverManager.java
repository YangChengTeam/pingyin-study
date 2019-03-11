package yc.com.pinyin_study.study.utils;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by wanglin  on 2018/11/17 09:11.
 * 观察者模式实践
 */
public class ObserverManager extends Observable {

    private static ObserverManager instance;


    public static ObserverManager getInstance() {
        synchronized (ObserverManager.class) {
            if (instance == null) {
                synchronized (ObserverManager.class) {
                    instance = new ObserverManager();
                }
            }
        }
        return instance;
    }


    public void addMyObserver(Observer observer) {
        addObserver(observer);
    }

    public void notifyMyObservers(Object object) {
        if (countObservers() > 0) {
            setChanged();
            notifyObservers(object);
        }
    }


    public void notifyMyObservers() {
        if (countObservers() > 0) {
            setChanged();
            notifyObservers();
        }
    }

    public void removeObserver(Observer observer) {
        deleteObserver(observer);

    }

    public void removeObservers() {
        deleteObservers();

    }


}
