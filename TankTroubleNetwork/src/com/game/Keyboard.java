package com.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.BitSet;

public class Keyboard implements KeyListener {
	public BitSet keys = new BitSet(160);
	public boolean up,down, left, right, space;
	public boolean E,S,D,F,Q;
	
	public void update(){
		up = keys.get(KeyEvent.VK_UP);
		E=keys.get(KeyEvent.VK_E);
		down = keys.get(KeyEvent.VK_DOWN); 
		D = keys.get(KeyEvent.VK_D);
		right = keys.get(KeyEvent.VK_RIGHT);
		F= keys.get(KeyEvent.VK_F);
		left = keys.get(KeyEvent.VK_LEFT);
		S= keys.get(KeyEvent.VK_S);
		space = keys.get(KeyEvent.VK_SPACE);
		Q = keys.get(KeyEvent.VK_Q);
	}
	public boolean getKey(byte KeySet, char a){
		if(KeySet==1){
			if(a == 'u'){
				return up;
			}
			else if(a== 'd'){
				return down;
			}
			else if(a== 'r'){
				return right;
			}
			else if(a== 'l'){
				return left;
			}
			else if(a== 's'){
				return space ;
			}
		}
		else if(KeySet==2){
			if(a == 'u'){
				return E;
			}
			else if(a== 'd'){
				return D;
			}
			else if(a== 'r'){
				return F;
			}
			else if(a== 'l'){
				return S;
			}
			else if(a== 's'){
				return Q ;
			}
		}
		return false;
	}
	public void keyTyped(KeyEvent e) {	
	}


	public void keyPressed(KeyEvent e) {
		keys.set(e.getKeyCode());
	}


	public void keyReleased(KeyEvent e) {
		keys.clear(e.getKeyCode());
	}

}
