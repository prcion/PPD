#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
#include <vector>
#include <chrono>
using namespace std;

void readFileNameFromConsole(string& fileName) {
	cout << "File name: ";
	cin >> fileName;
}

void readSizeFromConsole(int& size) {
	cout << "Size: ";
	cin >> size;
	while (size <= 0) {
		cout << "Size is invalid!" << endl << "Size: ";
		cin >> size;
	}
}

void readMinFromConsole(int& min) {
	cout << "Min: ";
	cin >> min;
}

void readMaxFromConsole(int& max, int min) {
	cout << "Max: ";
	cin >> max;

	while (max < min) {
		cout << "Max is invalid, max < min!" << endl << "Max: ";
		cin >> max;
	}
}

void readFromConsole(string& fileName, int& size, int& min, int& max) {
	readFileNameFromConsole(fileName);
	readSizeFromConsole(size);
	readMinFromConsole(min);
	readMaxFromConsole(max, min);
}

vector<int> readFromFile(string fileName) {
	ifstream ifile;
	ifile.open(fileName);
	vector<int> numbers;

	int num = 0;
	while (ifile >> num) {
		numbers.push_back(num);
	}

	ifile.close();

	sort(numbers.begin(), numbers.end());
	return numbers;
}

void verifyIfTwoVectorsContainsTheSameElements(vector<int> vectorOne, vector<int> vectorTwo) {
	bool vectorEquals = vectorOne == vectorTwo;
	if (vectorEquals) {
		cout << "fisierele contin aceleasi elemente";
	}
	else {
		cout << "fisierele nu contin aceleasi elemente";
	}
}

int main() {
	int command;
	cout << "Tasteaza 1 pentru a crea fisier.\nTasteaza 2 pentru a verifica daca doua fisiere contin aceleasi date.\n";
	cin >> command;
	if (command == 1) {
		ofstream ofile;

		string fileName;
		int size, min, max;
		readFromConsole(fileName, size, min, max);

		ofile.open(fileName);

		for (int i = 0; i < size; ++i) {
			ofile << rand() % (max - min + 1) + min << " ";
		}

		ofile.close();
	} else {
		string fileNameOne, fileNameTwo;
		readFileNameFromConsole(fileNameOne);
		readFileNameFromConsole(fileNameTwo);

		vector<int> numbersOne = readFromFile(fileNameOne);
		vector<int> numbersTwo = readFromFile(fileNameTwo);

		verifyIfTwoVectorsContainsTheSameElements(numbersOne, numbersTwo);
	}
}

