//
// Created by Ray Eldath on 2020/8/14.
//
#include <vector>
#include <ranges>
#include <iostream>

using namespace std;
using namespace std::views;

constexpr auto ranges_demo(initializer_list<int> l) {
    return l | filter([](int i) { return i % 2 == 0; })
           | take(5);
}

int main() {
    for (auto i : ranges_demo({1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 16}))
        cout << i << endl;
    return 0;
}