package config;

/**
 * @author 白日
 * @create 2024/3/12 22:04
 * @description
 */

public class Calculator {
    private int res;

    public Calculator add(int a){
        res += a;
        return this;
    }
    public Calculator sub(int a){
        res -= a;
        return this;
    }
    public void eliminate(){
        res = 0;
    }

    public int calculate(){
        return res;
    }
}
