package chase.minecraft.architectury.simplemodconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SimpleConfig
{
	int index() default 0;
	
	String displayName() default "";
	
	String description() default "";
	
	double min() default Double.MIN_VALUE;
	
	double max() default Double.MAX_VALUE;
	
	String[] options() default {};
}
