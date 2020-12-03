#include "bits/stdc++.h"

using namespace std;

void filterByProtocol(string protocolChoice){
    ifstream file("/home/near/Alpha/Stack/CNL_SEM5/A07/data.csv");
    string value,sr_no,time,source,destination,info,protocol,len;
    int count=0,i=0;

    cout<<setw(8)<<left<<"SrNo.";
    cout<<setw(16)<<left<<"Time";
    cout<<setw(32)<<left<<"Source";
    cout<<setw(32)<<left<<"Destination";
    cout<<setw(16)<<left<<"Protocol";
    cout<<setw(8)<<left<<"Length";
    cout<<"Info\n";
    while(file.good()){
        getline(file,sr_no,','); //GET STRING TILL ,
        getline(file,time,',');
        getline(file,source,',');
        getline(file,destination,',');
        getline(file,protocol,',');
        getline(file,len,',');
        getline(file,info,'\n');

        protocol=string(protocol,1,protocol.length()-2);

        if(protocol == protocolChoice){
            cout<<setw(8)<<left<<++i;
            cout<<setw(16)<<left<< string( time, 1, time.length()-2 );
            cout<<setw(32)<<left<<string( source, 1, source.length()-2 );
            cout<<setw(32)<<left<<string( destination, 1, destination.length()-2 );
            cout<<setw(16)<<left<<protocol;
            cout<<setw(8)<<left<< string( len, 1, len.length()-2 );
            cout<<string( info, 1, info.length()-2 )<<"\n";
            count++;
        }
    }
    file.close();
    cout<<"Total Packet Count: "<<count;
}

int main()
{
    int choice;

    cout<<"1. ICMPv6\n2. UDP\n3. TCP\n4. Ethernet\n0. Exit";
    cout<<"\nEnter Protocol: ";
    cin>>choice;

    while (choice!=0){
        switch(choice){
            case 1: filterByProtocol("ICMPv6");
                break;
            case 2: filterByProtocol("UDP");
                break;
            case 3: filterByProtocol("TCP");
                break;
            case 4: filterByProtocol("ARP");
                break;
            default:
                cout<<"\nInvalid Protocol Choice";
                break;
        }
        cout<<"\n\n1. ICMPv6\n2. UDP\n3. TCP\n4. Ethernet\n0. Exit";
        cout<<"\nEnter Protocol: ";
        cin>>choice;
    }
    return 0;
}

