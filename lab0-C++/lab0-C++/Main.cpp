#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
#include <vector>
#include <chrono>
using namespace std;

void readFromConsole(string& fileName, int& size, int& min, int& max) {
	cout << "File name: ";
	cin >> fileName;

	cout << "Size: ";
	cin >> size;
	while (size <= 0) {
		cout << "Size is invalid!" << endl << "Size: ";
		cin >> size;
	}

	cout << "Min: ";
	cin >> min;

	cout << "Max: ";
	cin >> max;

	while (max < min) {
		cout << "Max is invalid, max < min!" << endl << "Max: ";
		cin >> max;
	}
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
			ofile << rand() % max + min << " ";
		}

		ofile.close();
	} else {
		string fileNameOne, fileNameTwo;
		cout << "File one: ";
		cin >> fileNameOne;

		cout << "File two: ";
		cin >> fileNameTwo;
		
		auto start = std::chrono::system_clock::now();

		vector<int> numbersOne = readFromFile(fileNameOne);
		vector<int> numbersTwo = readFromFile(fileNameTwo);

		bool vectorEquals = numbersOne == numbersTwo;
		if (vectorEquals) {
			cout << "fisierele contin aceleasi elemente";
		} else {
			cout << "fisierele nu contin aceleasi elemente";
		}

		auto end = std::chrono::system_clock::now();
		cout << endl << (end - start).count();
	}
}

