package io.kaoto.forage.core.guardrails;

import io.kaoto.forage.core.common.BeanProvider;
import dev.langchain4j.guardrail.InputGuardrail;

/**
 * Provider interface for creating input guardrails.
 *
 * <p>Input guardrails validate user messages before they are processed by the AI model,
 * detecting issues like PII, prompt injection attacks, and other security concerns.
 *
 * @see dev.langchain4j.guardrail.InputGuardrail
 */
public interface InputGuardrailProvider extends BeanProvider<InputGuardrail> {}
