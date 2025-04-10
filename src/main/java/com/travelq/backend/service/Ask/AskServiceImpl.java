package com.travelq.backend.service.Ask;

import com.travelq.backend.repository.AskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AskServiceImpl implements AskService{
    private final AskRepository askRepository;


}
