#include "bits/stdc++.h"
using namespace std;

class Receiver{

private:
	int m,n;
	bool* message;
	bool* polynomial;
	
public:
	Receiver(int n,int m){
		this->n = n;
		this->m = m;
		message = new bool[n];
		polynomial = new bool[m];
	}
	
	void setPolynomial(string polynomial){
	for(int i=0; i<m; i++)
		this->polynomial[i] = polynomial[i]=='1';
	}
	
	void receive(bool *encodedMessage){
		this->message = encodedMessage;
	}
	
	bool* getMessage(){
		return message;
	}
	
	bool decode(){
		bool *temp = new bool[n+m-1];
		for(int i=0;i<n+m-1;i++)
			temp[i] = false;
		for(int i=0;i<n;i++)
			temp[i] = message[i];

		for(int i=0;i<n;i++){
     		if (polynomial[0]==temp[i]){
         		for(int j=0,k=i;j<m+1;j++,k++)
             		temp[k] = temp[k] xor polynomial[j];                		
     		} 
     	}
		
		bool error = false;
		for(int i=0;i<n+m-1;i++)
			error = error or temp[i];

			
		return error;
	}
	
};

