

public class Subnet {
    private String ipv4;
    private int[] octets;
    private int[] subnetMask;
    private int[] networkAddress;
    private int[] directBroadCastAddress;
    private char ipv4Class;
    private int defaultHostBits;
    private int subnets,hosts;
    private int addresses;

    Subnet(){
        this.ipv4 = "0.0.0.0";
        this.octets = new int[]{0, 0, 0, 0};
        this.ipv4Class = 'A';
        this.subnetMask = new int[]{255 ,0 ,0 ,0};
        this.defaultHostBits = 24;
    }

    Subnet(String ipv4){
        this();
        if(isValidIpv4(ipv4)){
            this.ipv4 = ipv4;
            String[] octets = ipv4.split("\\.");
            for (int i = 0; i < octets.length; i++)
                this.octets[i] = Integer.parseInt(octets[i]);

            if(this.octets[0]>=0 && this.octets[0]<=127) {
                this.ipv4Class = 'A';
                this.subnetMask = new int[]{255,0,0,0};
                this.defaultHostBits = 24;
            } else if(this.octets[0]>=128 && this.octets[0]<=191) {
                this.ipv4Class = 'B';
                this.subnetMask = new int[]{255, 255, 0, 0};
                this.defaultHostBits = 16;
            } else if(this.octets[0]>=192 && this.octets[0]<=223) {
                this.ipv4Class = 'C';
                this.subnetMask = new int[]{255, 255, 255, 0};
                this.defaultHostBits = 8;
            } else if(this.octets[0]>=224 && this.octets[0]<=239) {
                this.ipv4Class = 'D';
                this.subnetMask = new int[]{255, 255, 255, 255};
                this.defaultHostBits = 0;
            } else {
                this.ipv4Class = 'E';
                this.subnetMask = new int[]{255, 255, 255, 255};
                this.defaultHostBits = 0;
            }
        }
        this.addresses = (int)Math.pow(2,this.defaultHostBits);
    }

    Subnet(String ipv4,int addresses){
        this(ipv4);
        this.addresses = addresses;
        divide();
    }

    public boolean divide(){
        if(addresses > Math.pow(2, defaultHostBits)){
            return false;
        }
        int subnetHostBits = (int) Math.ceil(Math.log(addresses)/Math.log(2));
        String subnetHost = "";
        for(int i = 0; i< defaultHostBits; i++){
            if(i!=0 && i%8==0)
                subnetHost += '.';
            if(i < defaultHostBits-subnetHostBits)
                subnetHost += "1";
            else
                subnetHost += "0";
        }
        String[] subnetHostOctets = subnetHost.split("\\.");
        for(int i=subnetHostOctets.length-1,j=3; i>=0; i--,j--){
            this.subnetMask[j] = this.subnetMask[j] | Integer.parseInt(subnetHostOctets[i],2);
        }

        networkAddress = new int[4];
        directBroadCastAddress = new int[4];
        for(int i=0;i<4;i++) {
            networkAddress[i] = subnetMask[i] & this.octets[i];
            directBroadCastAddress[i] = networkAddress[i];
        }

        int offset = (int)Math.pow(2,subnetHostBits) - 1;
        int carry = 0;
        for(int i=3; i>=0; i--){
            directBroadCastAddress[i] = directBroadCastAddress[i] + offset%256 + carry;
            offset = offset/256;
            carry = directBroadCastAddress[i]/256;
            if(carry > 0)
                directBroadCastAddress[i] = 255;
        }

        int logSubnets = 0;
        for(int i=0;i<subnetHost.length();i++) {
            if (subnetHost.charAt(i) == '1')
                logSubnets++;
        }
        hosts = (int) Math.pow(2,subnetHostBits) - 2;
        subnets = (int) Math.pow(2,logSubnets);
        return true;
    }

    public static boolean isValidIpv4(String ipv4){
        if(!ipv4.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}"))
            return false;
        String[] octets = ipv4.split("\\.");
        for(String octet : octets)
            if(Integer.parseInt(octet)<0 || Integer.parseInt(octet)>255)
                return false;
        return true;
    }

    public String getIpv4() {
        return ipv4;
    }

    public int[] getOctets() {
        return octets;
    }

    public int[] getSubnetMask() {
        return subnetMask;
    }

    public int[] getNetworkAddress() {
        return networkAddress;
    }

    public int[] getDirectBroadCastAddress() {
        return directBroadCastAddress;
    }

    public char getIpv4Class() {
        return ipv4Class;
    }

    public int getDefaultHostBits() {
        return defaultHostBits;
    }

    public int getSubnets() {
        return subnets;
    }

    public int getHosts() {
        return hosts;
    }

    public int getAddresses() {
        return addresses;
    }

    public void setAddresses(int addresses) {
        this.addresses = addresses;
    }
}
