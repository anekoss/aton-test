package com.aton.app;

import lombok.Builder;


@Builder
public record Person(Long account, String name, Double value) {
}
