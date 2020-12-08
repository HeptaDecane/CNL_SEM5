set simulator [new Simulator] 
 
$simulator color 1 Blue 
$simulator color 2 Red 
 
set file [open out.nam w] 
$simulator namtrace-all $file

set file [open out.tr w] 
$simulator trace-all $file
 
proc finish {} { 
  global simulator file
  $simulator flush-trace 
  close $file
  exec nam out.nam
  exit 0
} 
 
set node0 [$simulator node] 
set node1 [$simulator node] 
set node2 [$simulator node] 
set node3 [$simulator node] 
 
$simulator duplex-link $node0 $node2 2Mb 10ms DropTail 
$simulator duplex-link $node1 $node2 2Mb 10ms DropTail 
$simulator duplex-link $node2 $node3 1.7Mb 20ms DropTail 
 
$simulator queue-limit $node2 $node3 10
$simulator duplex-link-op $node2 $node3 queuePos 0.5
 
$simulator duplex-link-op $node0 $node2 orient right-down 
$simulator duplex-link-op $node1 $node2 orient right-up 
$simulator duplex-link-op $node2 $node3 orient right 
 
# set up TCP and FTP 
set tcp [new Agent/TCP] 
$simulator attach-agent $node0 $tcp 
 
set sink [new Agent/TCPSink] 
$simulator attach-agent $node3 $sink 

$simulator connect $tcp $sink 
$tcp set fid_ 1
 
set ftp [new Application/FTP] 
$ftp attach-agent $tcp 
$ftp set type_ FTP 
 
# set up UDP and CBR  
set udp [new Agent/UDP] 
$simulator attach-agent $node1 $udp 
set null [new Agent/Null] 
 
$simulator attach-agent $node3 $null 
$simulator connect $udp $null 
$udp set fid_ 2
 
set cbr [new Application/Traffic/CBR] 
$cbr attach-agent $udp 
$cbr set type_ CBR 
$cbr set packet_size_ 1000
$cbr set rate_ 1mb
$cbr set random_ false 
 
$simulator at 0.1 "$cbr start"
$simulator at 1.0 "$ftp start"
$simulator at 4.0 "$ftp stop"
$simulator at 4.5 "$cbr stop"
$simulator at 4.5 "$simulator detach-agent $node0 $tcp ; $simulator detach-agent $node3 $sink"
$simulator at 5.0 "finish"

$simulator run 