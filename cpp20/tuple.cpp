//
// Created by Ray Eldath on 2020/8/8.
//

#include <tuple>
#include <string>
#include <iostream>

using namespace std;

tuple<int, int, int> something(int i) {
    return {i + 1, i + 2, i + 3};
}

int main() {
    int x, y;
    string z;
    tie(x, y, z) = something(12);
    cout << x << y << z << endl;
    return 0;
}