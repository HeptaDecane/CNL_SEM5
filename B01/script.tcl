set simulator [new Simulator]

$simulator color 1 Blue 

set nam_file [open out.nam w]
$simulator namtrace-all $nam_file

set tr_file [open out.tr w]
$simulator trace-all $tr_file

proc finish {} {
	global simulator nam_file tr_file
	$simulator flush-trace
	close $nam_file
	close $tr_file
	exec nam out.nam
	exit 0
}

set node0 [$simulator node]
set node1 [$simulator node]
set node2 [$simulator node]

$simulator duplex-link $node0 $node1 2Mb 20ms DropTail
$simulator duplex-link $node1 $node2 1Mb 40ms DropTail

$simulator duplex-link-op $node0 $node1 orient right-down
$simulator duplex-link-op $node1 $node2 orient right-up

$simulator queue-limit $node1 $node2 5
$simulator duplex-link-op $node1 $node2 queuePos -0.5

set tcp [new Agent/TCP]
$simulator attach-agent $node0 $tcp

set sink [new Agent/TCPSink]
$simulator attach-agent $node2 $sink

$simulator connect $tcp $sink
$tcp set fid_ 1

set ftp [new Application/FTP]
$ftp attach-agent $tcp
$ftp set type_ FTP

$simulator at 0.1 "$ftp start"
$simulator at 4.9 "$ftp stop"

$simulator at 4.9 "$simulator detach-agent $node0 $tcp"
$simulator at 4.9 "$simulator detach-agent $node2 $sink"

$simulator at 5.0 "finish"

$simulator run