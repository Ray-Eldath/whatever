//
// Created by Ray Eldath on 2020/9/18.
//
#include <iostream>
#include <vector>
#include <functional>
#include <thread>
#include <future>

auto doWorkThread(const std::function<bool(int)> &filter, int maxVal, int magicValue = 0) {
    auto passed = std::vector<int>();

    std::thread t{[&filter, maxVal, &passed] {
        for (int i = 0; i < maxVal; i++)
            if (filter(i))
                passed.push_back(i);
    }};

    if (magicValue == 0) {
        t.join();
        return passed;
    }

    return std::vector<int>{};
}

auto doWorkAsync(const std::function<bool(int)> &filter, int maxVal) {
    return std::async(std::launch::async,
                      [&filter, maxVal] {
                          auto passed = std::vector<int>();
                          for (int i = 0; i < maxVal; i++)
                              if (filter(i))
                                  passed.emplace_back(i);

                          return passed;
                      });
}

void doWorkPromise(std::promise<std::vector<int>> promise, const std::function<bool(int)> &filter, int maxVal) {
    auto passed = std::vector<int>();
    for (int i = 0; i < maxVal; i++)
        if (filter(i))
            passed.emplace_back(i);

    promise.set_value(passed);
}

int main() {
    std::cout << "async:" << std::endl;
    for (auto i : doWorkAsync([](int v) { return v % 2 != 0; }, 100).get())
        std::cout << i << " ";
    std::cout << std::endl;

    std::cout << "promise:" << std::endl;
    std::promise<std::vector<int>> promise;
    auto future = promise.get_future();
    std::thread work_thread(doWorkPromise, std::move(promise), [](int v) { return v % 2 != 0; }, 100);
    for (auto i : future.get())
        std::cout << i << " ";
    std::cout << std::endl;
    work_thread.join();

    std::cout << "thread:" << std::endl;
    for (auto i : doWorkThread([](int v) { return v % 2 == 0; }, 100))
        std::cout << i << " ";
    std::cout << std::endl;

    std::cout << "thread broken:" << std::endl;
    for (auto i : doWorkThread([](int v) { return v % 2 == 0; }, 100, 1))
        std::cout << i << " ";

    return 0;
}