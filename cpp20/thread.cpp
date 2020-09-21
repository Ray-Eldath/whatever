//
// Created by Ray Eldath on 2020/9/18.
//
#include <iostream>
#include <vector>
#include <functional>
#include <thread>
#include <future>

auto doWorkThread(std::function<bool(int)> filter, int maxVal, int magicValue = 0) {
    auto passed = std::vector<int>();

    std::thread t{[&filter, maxVal, &passed] {
        for (int i = 0; i < maxVal; i++) {
            if (filter(i))
                passed.push_back(i);
        }
    }};

    if (magicValue == 0) {
        t.join();
        return passed;
    }

    return std::vector<int>{};
}

auto doWorkAsync(std::function<bool(int)> filter, int maxVal) {
    auto passed = std::vector<int>();

    return std::async(std::launch::async,
                      [&filter, maxVal, &passed] {
                          for (int i = 0; i < maxVal; i++) {
                              if (filter(i))
                                  passed.push_back(i);
                          }
                      });
}

int main() {
    for (auto i : doWorkThread([](int v) { return v % 2 == 0; }, 100))
        std::cout << i << " ";
    std::cout << std::endl;

    for (auto i : doWorkThread([](int v) { return v % 2 != 0; }, 100, 1))
        std::cout << i << " ";
    std::cout << std::endl;

    return 0;
}