//
// Created by Ray Eldath on 2020/9/4.
//

#include <cstdlib>
#include <iostream>

template<typename T, std::size_t N>
constexpr int size_of(const T(&)[N]) {
    return N;
};

int main() {
    int a[] = {1, 2, 3};
    int b[size_of(a)];
    std::cout << size_of(a);
}