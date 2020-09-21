//
// Created by Ray Eldath on 2020/8/7.
//
#include <exception>
#include <iostream>

int do_something(int i) {
    int r;
    if (i > 15) {
        throw std::exception();
    } else {
        r = 125;
    }
    return r;
}

int main() {
    int n;
    std::cin >> n;
    try {
        std::cout << do_something(n) << std::endl;
    } catch (std::exception &exp) {
        std::cout << exp.what() << std::endl;
    }
    return 0;
}