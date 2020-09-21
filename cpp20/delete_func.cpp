//
// Created by Ray Eldath on 2020/9/6.
//
#include <iostream>

bool isLuckyNumber(int i) noexcept {
    return i % 13 == 0;
}

bool isLuckyNumber(bool c) = delete;
bool isLuckyNumber(double c) = delete;
bool isLuckyNumber(char c) = delete;

int main() {
    std::cout << isLuckyNumber(13) << std::endl;
//    std::cout << isLuckyNumber(true) << std::endl;
//    std::cout << isLuckyNumber(4.5) << std::endl;
//    std::cout << isLuckyNumber('c') << std::endl;
    return 0;
}