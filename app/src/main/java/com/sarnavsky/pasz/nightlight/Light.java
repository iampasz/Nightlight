package com.sarnavsky.pasz.nightlight;

import java.io.Serializable;

/**
 * Created by pasz on 27.10.2016.
 */

//1. создай класс Light, с констуктором (инт, инт), туда будешь передавать ид картинки и ид строки
//2. создай аррейЛист и добавь в него новые экземпляры Light и нужными тебе картинками и именами
//3. напиши метод который будет создавать новый фрагмент, а в аргументы ему передай класс Light
//4. в твой адаптер должен принимать список фрагментов
//5. в фрагментн доставай из аргументов Light из него картинку и текст и добавляй в поля

public class Light implements Serializable{
    int mypic;
    int mytext;

    public  Light(int _mypic, int _mytext){
        mypic = _mypic;
        mytext = _mytext;

    }
}






