#include "bits/stdc++.h"
#include "ctime"
#include "Sender.cpp"
#include "Receiver.cpp"
using namespace std;


int main(){
	srand((int)time(0));
	
	string message, polynomial;
	cout<<"Enter Message: ";
	cin>>message;
	cout<<"Enter Polynomial: ";
	cin>>polynomial;
	
	int n = message.length();
	int m = polynomial.length();
	Sender sender(n,m);
	Receiver receiver(n+m-1,m);
	
	sender.setPolynomial(polynomial);
	receiver.setPolynomial(polynomial);
	sender.setMessage(message);
	
	sender.encode();
	
	bool* crc = sender.getCRC();
	cout<<"\nCRC: ";
	for(int i=0;i<m-1;i++)
		cout<<crc[i]<<" ";
	
	bool* encodedMessage = sender.getEncodedMessage();
	cout<<"\nTransmitted Message:  ";
	for(int i=0;i<n+m-1;i++)
		cout<<encodedMessage[i]<<" ";
		
	int choice;
	cout<<"\n\nAdd Error? (0/1): ";
	cin>>choice;
	if(choice==1){
		int i = rand()%n;
		encodedMessage[i] = not encodedMessage[i];
	}
	
	receiver.receive(encodedMessage);
	
	bool* receivedMessage = receiver.getMessage();	
	cout<<"\nReceived Message:     ";
	for(int i=0;i<n+m-1;i++)
		cout<<receivedMessage[i]<<" ";
	
	if(receiver.decode())
		cout<<"\nerror detected in received message";
	else
		cout<<"\nno error in received message";
	
	cout<<endl;
	return 0;
}

