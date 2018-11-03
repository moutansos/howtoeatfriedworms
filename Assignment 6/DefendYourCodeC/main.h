char* promptForValidText(char buff[], const int bufferSize, const char promptText[], const char regexString[]);
long promptForValidInteger(const char promptText[]);
char* promptForValidFileName(char buff[], const int bufferSize, const char promptFile[], const char regexFile[], const bool fileMustAlreadyExist);
void storePassword(char * firstPassword);
void comparePasswords(char * secondPassword);
void outputInfoToFile(char * firstName, char * lastName, int val1, int val2);
