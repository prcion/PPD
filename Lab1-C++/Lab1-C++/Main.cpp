#include <iostream>
#include <string>
#include <thread>
#include <vector>
#include <fstream>
#include <chrono>
#include <atomic>

using namespace std;
using std::chrono::high_resolution_clock;
using std::chrono::duration_cast;

#define N 10
#define M 10000
#define kernelN 5
#define kernelM 5

atomic<int> nr;
int threadNr;

void readFromFileMatrix(int matrix[N][M], string fileName) {
	ifstream ifile;
	ifile.open(fileName);

	int n = 0, m = 0;
	int num;
	while (ifile >> num) {
		if (m == M) {
			n++;
			m = 0;
		}
		else {
			matrix[n][m] = num;
			m++;
		}
		if (n == N - 1 && m == M) break;
	}
	ifile.close();
}

void readFromFileMatrixDinamic(int** matrix, string fileName) {
	ifstream ifile;
	ifile.open(fileName);

	int n = 0, m = 0;
	int num;
	while (ifile >> num) {
		if (m == M) {
			n++;
			m = 0;
		}
		else {
			matrix[n][m] = num;
			m++;
		}
		if (n == N - 1 && m == M) break;
	}
	ifile.close();
}

void readFromFileKernel(int matrix[kernelN][kernelM], string fileName) {
	ifstream ifile;
	ifile.open(fileName);

	int n = 0, m = 0;
	int num;
	while (ifile >> num) {
		if (m == kernelM) {
			n++;
			m = 0;
		}
		else {
			matrix[n][m] = num;
			m++;
		}
		if (n == kernelN - 1 && m == kernelM) break;
	}
	ifile.close();
}

void readFromFileKernelDinamic(int** matrix, string fileName) {
	ifstream ifile;
	ifile.open(fileName);

	int n = 0, m = 0;
	int num;
	while (ifile >> num) {
		if (m == kernelM) {
			n++;
			m = 0;
		}
		else {
			matrix[n][m] = num;
			m++;
		}
		if (n == kernelN - 1 && m == kernelM) break;
	}
	ifile.close();
}
int calculateFilterKernel(int kernel[kernelN][kernelM], int matrix[N][M], int n, int m) {
	int result = 0;

	for (int k = 0; k < kernelN; ++k) {
		for (int l = 0; l < kernelM; ++l) {
			if (n - k < 0) continue;
			if (m - l < 0) continue;
			result += matrix[n - k][m - l] * kernel[k][l];
		}
	}
	return result;
}

void calculateThreadSubmatrix(int start, int end, int kernel[kernelN][kernelM], int matrix[N][M]) {
	int *array = new int[start + end];
	int p = 0;
	for (int k = start; k < end; ++k) {
		int i = k / M;
		int j = k % M;
		array[p] = calculateFilterKernel(kernel, matrix, i, j);
		p++;
	}
	nr++;
	while (true) {
		if (nr == threadNr) {
			p = 0;
			for (int k = start; k < end; ++k) {
				int i = k / M;
				int j = k % M;
				matrix[i][j] = array[p];
				p++;
			}
			delete[] array;
			break;
		}
	}
}

void threadSubmatrix(int nrThreads, int kernel[kernelN][kernelM], int matrix[N][M]) {
	int n = N * M;
	int size = n / nrThreads;
	int rest = n % nrThreads;
	int start = 0, end;

	vector<thread> threads(nrThreads);

	for (int i = 0; i < nrThreads; ++i) {
		end = start + size;
		if (rest > 0) {
			end++;
			rest--;
		}

		threads[i] = thread(calculateThreadSubmatrix, start, end, kernel, matrix);

		start = end;
	}

	for (int i = 0; i < nrThreads; ++i) {
		threads[i].join();
	}
}

void calculateStatic(int threads) {
	int matrix[N][M], kernel[kernelN][kernelM];
	readFromFileMatrix(matrix, "date.txt");
	readFromFileKernel(kernel, "date_kernel.txt");
	auto start = chrono::high_resolution_clock::now();
	threadSubmatrix(threads, kernel, matrix);
	auto finish = chrono::high_resolution_clock::now();
	cout << (double)chrono::duration_cast<chrono::nanoseconds>(finish - start).count() / 1E6;
}

int calculateFilterKernelDinamic(int** kernel, int** matrix, int n, int m);
void calculateThreadSubmatrixDinamic(int start, int end, int** kernel, int** matrix);
void threadSubmatrixDinamic(int nrThreads, int** kernel, int** matrix);
void calculateDinamic(int threads);

int main(int argc, char**argv) {
	//threadNr = stoi( argv[1] );
	threadNr = 8;
	//calculateStatic(threadNr);
	calculateDinamic(threadNr);
}

void calculateDinamic(int threads) {
	int** matrix = new int*[N];
	for (int i = 0; i < N; ++i)
		matrix[i] = new int[M];

	int** kernel = new int*[kernelN];
	for (int i = 0; i < kernelN; ++i)
		kernel[i] = new int[kernelM];

	readFromFileMatrixDinamic(matrix, "date.txt");
	readFromFileKernelDinamic(kernel, "date_kernel.txt");
	auto start = chrono::high_resolution_clock::now();
	threadSubmatrixDinamic(threads, kernel, matrix);
	auto finish = chrono::high_resolution_clock::now();
	cout << (double)chrono::duration_cast<chrono::nanoseconds>(finish - start).count() / 1E6;

	for (int i = 0; i < N; ++i) {
		delete[] matrix[i];
	}
	delete[] matrix;

	for (int i = 0; i < kernelN; ++i)
		delete[] kernel[i];
	delete[] kernel;
}

void threadSubmatrixDinamic(int nrThreads, int** kernel, int** matrix) {
	int n = N * M;
	int size = n / nrThreads;
	int rest = n % nrThreads;
	int start = 0, end;

	vector<thread> threads(nrThreads);
	for (int i = 0; i < nrThreads; ++i) {
		end = start + size;
		if (rest > 0) {
			end++;
			rest--;
		}

		threads[i] = thread(calculateThreadSubmatrixDinamic, start, end, kernel, matrix);

		start = end;
	}

	for (int i = 0; i < nrThreads; ++i) {
		threads[i].join();
	}
}

void calculateThreadSubmatrixDinamic(int start, int end, int** kernel, int** matrix) {
	int *array = new int[start + end];
	int p = 0;
	for (int k = start; k < end; ++k) {
		int i = k / M;
		int j = k % M;
		array[p] = calculateFilterKernelDinamic(kernel, matrix, i, j);
		p++;
	}
	nr++;
	while (true) {
		if (nr == threadNr) {
			p = 0;
			for (int k = start; k < end; ++k) {
				int i = k / M;
				int j = k % M;
				matrix[i][j] = array[p++];
			}
			delete[] array;
			break;
		}
	}
}

int calculateFilterKernelDinamic(int** kernel, int** matrix, int n, int m) {
	int result = 0;

	for (int k = 0; k < kernelN; ++k) {
		for (int l = 0; l < kernelM; ++l) {
			if (n - k < 0) continue;
			if (m - l < 0) continue;
			result += matrix[n - k][m - l] * kernel[k][l];
		}
	}
	return result;
}