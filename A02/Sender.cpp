#include "bits/stdc++.h"
using namespace std;

class Sender{

private:
	bool *message;
	bool *polynomial;
	bool *crc;
	bool *encodedMessage;
	int m,n;
	
public:
	Sender(int n, int m){
		this->m = m;
		this->n = n;
		message = new bool[n];
		polynomial = new bool[m];
		crc = new bool[m-1];
		encodedMessage = new bool[n+m-1];
	}
	
	void encode(){
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
     	
     	for(int i=n,j=0;i<m+n-1;j++,i++)
     		crc[j] = temp[i],
     		encodedMessage[i] = temp[i];
     		

     	for(int i=0;i<n;i++)
     		encodedMessage[i] = message[i];
	}
	
	bool* getCRC(){
		return crc;
	}
	
	bool* getEncodedMessage(){
		return encodedMessage;
	}

	bool setMessage(string message){
		for(int i=0; i<n; i++)
			this->message[i] = message[i]=='1';
	}
	
	void setPolynomial(string polynomial){
		for(int i=0; i<m; i++)
			this->polynomial[i] = polynomial[i]=='1';
	}
};

