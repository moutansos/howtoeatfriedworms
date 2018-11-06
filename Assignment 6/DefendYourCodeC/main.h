void promptForValidText(char buff[], const size_t bufferSize, const char promptText[], const char regexString[]);
long promptForValidInteger(const char promptText[]);
void promptForValidFileName(char buff[], const int bufferSize, const char promptFile[], const char regexFile[], const bool fileMustAlreadyExist);
void storePassword(char * firstPassword);
void comparePasswords(char * secondPassword, size_t passwordBufferSize);
void outputInfoToFile(char * firstName, char * lastName, const long long int val1, const long long int val2, char * readFileName, char * writeFileName);
void clearBuffer(char *input);
long hash(char * password);
void generateSalt(char saltOutputBuff[], const size_t bufferSize);
char* encode(const char* input);
void PBKDF2_HMAC_SHA_512(const char* pass,
                         const size_t passLength,
                         const unsigned char* salt,
                         const size_t saltLength, 
                         const int32_t iterations, 
                         const uint32_t outputBytes, 
                         char* hexResult, 
                         uint8_t* binResult);